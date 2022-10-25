# Create Pub/Sub Topic
resource "google_pubsub_topic" "nibas-events" {
  name = "nibas-events"
}

# Service Account for nibas-backend
resource "google_service_account" "nibas-events-publisher-sa" {
  account_id   = "nibas-backend-publisher"
  display_name = "nibas-backend-publisher"
  description  = "SA for publishing events"
}

resource "google_service_account_iam_member" "sa_iam_impersonate" {
  service_account_id = google_service_account.nibas-events-publisher-sa.name
  role               = "roles/iam.workloadIdentityUser"
  member             = "serviceAccount:${google_service_account.nibas-events-publisher-sa.email}"
}

resource "google_pubsub_topic_iam_binding" "nibas-events-publisher" {
  topic   = google_pubsub_topic.nibas-events.name
  role    = "roles/pubsub.publisher"
  members = ["serviceAccount:${google_service_account.nibas-events-publisher-sa.email}"]
}

# The subscriber-application nibas-events
module "nibas-events-subscriber" {
  source          = "./subscriber"
  subscriber_name = "nibas-events-subscriber"
  topic           = google_pubsub_topic.nibas-events.name
  iam_impersonate = true
}
