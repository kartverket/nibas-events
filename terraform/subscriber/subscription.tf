resource "google_pubsub_subscription" "subscription" {
  name  = var.subscriber_name
  topic = var.topic
}

resource "google_service_account" "nibas-events-subscriber-sa" {
  account_id   = var.subscriber_name
  display_name = var.subscriber_name
  description  = "Service Account for the subscriber ${var.subscriber_name} of the topic ${var.topic}"
}

resource "google_pubsub_subscription_iam_member" "subscriber" {
  subscription = google_pubsub_subscription.subscription.name
  role         = "roles/pubsub.subscriber"
  member       = "serviceAccount:${google_service_account.nibas-events-subscriber-sa.email}"
}

# Add possibility to impersonate this service account only if iam_impersonate = true
resource "google_service_account_iam_member" "subscriber_iam_impersonate" {
  count              = var.iam_impersonate ? 1 : 0
  service_account_id = google_service_account.nibas-events-subscriber-sa.name
  role               = "roles/iam.workloadIdentityUser"
  member             = var.iam_kubernetes_sa
}

output "sa-email" {
  value = google_service_account.nibas-events-subscriber-sa.email
}
