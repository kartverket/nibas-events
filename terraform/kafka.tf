resource "kubernetes_persistent_volume_claim" "nibas-kafka-pvc-data" {
  metadata {
    name      = "nibas-kafka-pvc-data"
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

resource "kubernetes_stateful_set" "nibas-kafka" {
  metadata {
    namespace = local.namespace
    name      = "nibas-kafka"
  }

  spec {
    pod_management_policy  = "Parallel"
    replicas               = 1
    revision_history_limit = 5

    selector {
      match_labels = {
        app = "nibas-kafka"
      }
    }

    service_name = "nibas-kafka"

    template {
      metadata {
        annotations = {
          "seccomp.security.alpha.kubernetes.io/pod" = "runtime/default"
        }
        labels = {
          app = "nibas-kafka"
        }
      }

      spec {
        image_pull_secrets {
          name = "nibas-pull-token-atkv1-kes"
        }
        security_context {
          supplemental_groups = [1000]
          fs_group            = 1000
        }

        volume {
          name = "nibas-kafka-pv-vol-data"
          persistent_volume_claim {
            claim_name = "nibas-kafka-pvc-data"
          }
        }

        volume {
          name = "nibas-kafka-etc"
          empty_dir {}
        }

        volume {
          name = "nibas-kafka-var-log"
          empty_dir {}
        }

        container {
          name              = "nibas-kafka"
          image             = "confluentinc/cp-kafka:latest"
          image_pull_policy = "IfNotPresent"

          security_context {
            privileged                 = false # Normal priviliges
            allow_privilege_escalation = false # Prevent reqests for root priviliges
            read_only_root_filesystem  = true
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
            name  = "KAFKA_BROKER_ID"
            value = 1
          }

          env {
            name  = "KAFKA_ZOOKEEPER_CONNECT"
            value = "${kubernetes_service.nibas-zookeeper-service.metadata.0.name}:${kubernetes_service.nibas-zookeeper-service.spec.0.port.0.port}"
          }

          env {
            name  = "KAFKA_ADVERTISED_LISTENERS"
            value = "PLAINTEXT://nibas-kafka:9092"
          }

          env {
            name  = "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP"
            value = "PLAINTEXT:PLAINTEXT"
          }

          env {
            name  = "KAFKA_INTER_BROKER_LISTENER_NAME"
            value = "PLAINTEXT"
          }

          env {
            name  = "KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR"
            value = 1
          }

          port {
            container_port = 9092
          }

          volume_mount {
            name       = "nibas-kafka-pv-vol-data"
            mount_path = "/var/lib/kafka"
          }

          volume_mount {
            name       = "nibas-kafka-etc"
            mount_path = "/etc/kafka"
          }

          volume_mount {
            name       = "nibas-kafka-var-log"
            mount_path = "/var/log"
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

resource "kubernetes_service" "nibas-kafka-service" {
  metadata {
    name      = "nibas-kafka"
    namespace = local.namespace
  }
  spec {
    selector = {
      app = "nibas-kafka"
    }
    port {
      protocol    = "TCP"
      port        = 9092
      target_port = 9092
    }
    type = "ClusterIP"
  }
}
