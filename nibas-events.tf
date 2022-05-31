terraform {
  backend "gcs" {
    prefix = "nibas-events"
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

locals {
  namespace = "nibas"
}

variable "nibas_events_version" {}
variable "ENVIRONMENT" {}

resource "kubernetes_deployment" "nibas-events-deployment" {
  metadata {
    name      = "nibas-events"
    namespace = local.namespace
    labels = {
      "backstage.io/kubernetes-id" = "nibas"
    }
  }
  spec {
    replicas = 3
    selector {
      match_labels = {
        app = "nibas-events"
      }
    }
    template {
      metadata {
        annotations = {
          "prometheus.io/scrape" = "true"
          "seccomp.security.alpha.kubernetes.io/pod" = "runtime/default"
        }
        labels = {
          app = "nibas-events"
          "backstage.io/kubernetes-id" = "nibas"
        }
      }
      spec {
        image_pull_secrets {
          # Midlertidig PAT for Kenneth
          name="nibas-pull-token-atkv1-kes"
        }
        security_context {
          supplemental_groups = [199]
          fs_group = 199
        }
        container {
          image = "ghcr.io/kartverket/nibas-events:${var.nibas_events_version}"
          name  = "nibas-events"
          security_context {
            privileged                 = false # Normal priviliges
            allow_privilege_escalation = false # Prevent reqests for root priviliges
            read_only_root_filesystem  = true  # Prevent writing to system files
            run_as_user                = 199   # Run as an unpriviliged user
            run_as_group               = 199   # Run as an unpriviliged group
          }
          resources {
            requests = {
              memory = "1Gi"
            }
            limits = {
              memory = "1Gi"
            }
          }
          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = var.ENVIRONMENT
          }
          port {
            container_port = 8080
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "nibas-events-service" {
  metadata {
    name      = "nibas-events"
    namespace = local.namespace
  }
  spec {
    selector = {
      app = "nibas-events"
    }
    port {
      protocol    = "TCP"
      port        = 80
      target_port = 8080
    }
    type     = "ClusterIP"
  }
}

resource "kubernetes_manifest" "istio-destination-rule" {
  manifest = yamldecode(file("${path.module}/kubernetes/destination-rule.yaml"))
}

resource "kubernetes_manifest" "istio-gateway" {
  manifest = yamldecode(file("${path.module}/kubernetes/gateway.yaml"))
}

resource "kubernetes_manifest" "istio-virtualservice" {
  manifest = yamldecode(file("${path.module}/kubernetes/virtualservice.yaml"))
}
