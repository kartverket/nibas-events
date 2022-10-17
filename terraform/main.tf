terraform {
  backend "gcs" {
    prefix = "nibas-events"
  }

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.39.0"
    }
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "google" {
  project = var.nibas_project_id
}

locals {
  namespace = "nibas"
}

