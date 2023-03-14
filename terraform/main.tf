terraform {
  backend "gcs" {
    prefix = "nibas-events"
  }

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.57.0"
    }
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "google" {
  project = var.NIBAS_PROJECT_ID
  region  = var.GCP_REGION
}

provider "vault" {
  address         = var.VAULT_ADDR
  skip_tls_verify = true
}

locals {
  namespace = "nibas"
}

