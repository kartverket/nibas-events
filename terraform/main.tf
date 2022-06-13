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
