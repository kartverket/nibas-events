data "vault_generic_secret" "nibas_db_details" {
  path = "nibas/nibas-events-db"
}

resource "random_password" "consumer_api_key" {
  length  = 29
  special = true
  lower   = true
  upper   = true
  numeric = true
}

resource "random_password" "publisher_api_key" {
  length  = 29
  special = true
  lower   = true
  upper   = true
  numeric = true
}

resource "vault_generic_secret" "nibas_events_api_keys_vault" {
  path = "nibas/nibas-events-apikeys"

  data_json = <<EOT
{
  "api.key.consumer":   "${random_password.consumer_api_key.result}",
  "api.key.publisher":  "${random_password.publisher_api_key.result}"
}
EOT
}

resource "kubernetes_manifest" "nibas_events_application" {
  manifest = {
    apiVersion = "skiperator.kartverket.no/v1alpha1"
    kind       = "Application"
    metadata = {
      name      = "nibas-events"
      namespace = "nibas"
    }
    spec = {
      image = "ghcr.io/kartverket/nibas-events:${var.NIBAS_EVENTS_VERSION}"
      port  = 8080

      ingresses = [var.EXTERNAL_DNS_HOSTNAME]
      replicas = {
        min                  = 3
        max                  = 3
        targetCpuUtilization = 80
      }


      env = [
        {
          name  = "KUBERNETES_CLUSTER"
          value = var.KUBERNETES_CLUSTER
        }
      ]
      strategy = { type = "RollingUpdate" }

      # Liveness probes define a resource that returns 200 OK when the app is running
      # as intended. Returning a non-200 code will make kubernetes restart the app.
      # Liveness is optional, but when provided path and port is required
      liveness = {
        path             = "/actuator/health/liveness"
        port             = 8080
        failureThreshold = 3
        timeout          = 5
        initialDelay     = 90
      }

      readiness = {
        path             = "/actuator/health/readiness"
        port             = 8080
        failureThreshold = 3
        timeout          = 5
        initialDelay     = 90
      }

      resources = {
        limits = {
          memory = "1G"
        }
        requests = {
          cpu    = "10m"
          memory = "500M"
        }
      }

      accessPolicy = {
        inbound = {
          rules = [
            {
              application = "nibas-backend"
              namespace   = "nibas"
            },
            {
              application = "nibas-backend"
              namespace   = "nibas-main"
            },
            {
              application = "dataplattform-proxy"
              namespace   = "dataplattform"
            }
          ]
        }
        outbound = {
          rules = [
            {
              application = "vault"
              namespace   = "vault"
            }
          ]

          external = [
            {
              host = data.vault_generic_secret.nibas_db_details.data["db-host"]
              ip   = data.vault_generic_secret.nibas_db_details.data["db-ip"]
              ports = [
                {
                  name     = "PostgisPort"
                  protocol = "TCP"
                  port : 5432
                }
              ]
            }
          ]
        }

      }
    }
  }
  field_manager {
    force_conflicts = true
  }
}
