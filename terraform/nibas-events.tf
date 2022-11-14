data "vault_generic_secret" "nibas_db_details" {
  path = "nibas/nibas-events-db"
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
      gcp = {
        auth = {
          serviceAccount = module.nibas-events-subscriber.sa-email
        }
      }
      replicas = {
        min                  = 1
        max                  = 2
        targetCpuUtilization = 80
      }


      env = [
        {
          name  = "KUBERNETES_CLUSTER"
          value = var.KUBERNETES_CLUSTER
        },
        {
          name  = "GOOGLE_CLOUD_PROJECT"
          value = var.NIBAS_PROJECT_ID
        }
      ]
      strategy = { type = "RollingUpdate" }

      # Liveness probes define a resource that returns 200 OK when the app is running
      # as intended. Returning a non-200 code will make kubernetes restart the app.
      # Liveness is optional, but when provided path and port is required
      liveness = {
        path             = "/actuator/health"
        port             = 8080
        failureThreshold = 3
        timeout          = 1
        initialDelay     = 60
      }

      readiness = {
        path = "/actuator/health"
        port = 8080
      }

      resources = {
        limits = {
          cpu    = "1000m"
          memory = "1G"
        }
        requests = {
          cpu    = "10m"
          memory = "500M"
        }
      }

      accessPolicy = {
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
}
