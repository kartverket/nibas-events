resource "kubernetes_persistent_volume_claim" "nibas-zookeeper-pvc-data" {
  metadata {
    name      = "nibas-zookeeper-pvc-data"
    namespace = local.namespace
  }
  spec {
    storage_class_name = "standard-rwo"
    access_modes       = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "1Gi"
      }
    }
  }
}

resource "kubernetes_persistent_volume_claim" "nibas-zookeeper-pvc-logs" {
  metadata {
    name      = "nibas-zookeeper-pvc-logs"
    namespace = local.namespace
  }
  spec {
    storage_class_name = "standard-rwo"
    access_modes       = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "1Gi"
      }
    }
  }
}

resource "kubernetes_persistent_volume_claim" "nibas-zookeeper-etc" {
  metadata {
    name      = "nibas-zookeeper-etc"
    namespace = local.namespace
  }
  spec {
    storage_class_name = "standard-rwo"
    access_modes       = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "200M"
      }
    }
  }
}

resource "kubernetes_stateful_set" "nibas-zookeeper" {
  metadata {
    namespace = local.namespace
    name = "nibas-zookeeper"
  }

  spec {
    pod_management_policy  = "Parallel"
    replicas               = 1
    revision_history_limit = 5

    selector {
      match_labels = {
        k8s-app = "nibas-zookeeper"
      }
    }

    service_name = "nibas-zookeeper"

    template {
      metadata {
        annotations = {
          "seccomp.security.alpha.kubernetes.io/pod" = "runtime/default"
        }
        labels = {
          k8s-app = "nibas-zookeeper"
        }
      }

      spec {
        image_pull_secrets {
          name="nibas-pull-token-atkv1-kes"
        }
        security_context {
          supplemental_groups = [1000]
          fs_group = 1000
        }

        volume {
          name = "nibas-zookeeper-pv-vol-data"
          persistent_volume_claim {
            claim_name = "nibas-zookeeper-pvc-data"
          }
        }

        volume {
          name = "nibas-zookeeper-pv-vol-logs"
          persistent_volume_claim {
            claim_name = "nibas-zookeeper-pvc-logs"
          }
        }

        volume {
          name = "nibas-zookeeper-etc"
          persistent_volume_claim {
            claim_name = "nibas-zookeeper-etc"
          }
        }

        container {
          name              = "nibas-zookeeper"
          image             = "confluentinc/cp-zookeeper:latest"
          image_pull_policy = "IfNotPresent"

          security_context {
            privileged                 = false # Normal priviliges
            allow_privilege_escalation = false # Prevent reqests for root priviliges
            read_only_root_filesystem  = true  # Prevent writing to system files
            run_as_user                = 1000   # Run as an unpriviliged user
            run_as_group               = 1000   # Run as an unpriviliged group
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
            name = "ZOOKEEPER_CLIENT_PORT"
            value = 2181
          }

          env {
            name = "ZOOKEEPER_TICK_TIME"
            value = 2000
          }

          port {
            container_port = 2181
          }

          volume_mount {
            name       = "nibas-zookeeper-pv-vol-data"
            mount_path = "/var/lib/zookeeper/data"
          }
          volume_mount {
            name       = "nibas-zookeeper-pv-vol-logs"
            mount_path = "/var/lib/zookeeper/log"
          }
          volume_mount {
            name       = "nibas-zookeeper-etc"
            mount_path = "/etc/kafka"
          }
        }
      }
    }

    update_strategy {
      type = "RollingUpdate"

      rolling_update {
        partition = 1
      }
    }
  }
}

resource "kubernetes_service" "nibas-zookeeper-service" {
  metadata {
    name      = "nibas-zookeeper"
    namespace = local.namespace
  }
  spec {
    selector = {
      app = "nibas-zookeeper"
    }
    port {
      protocol    = "TCP"
      port        = 22181
      target_port = 2181
    }
    type     = "ClusterIP"
  }
}
