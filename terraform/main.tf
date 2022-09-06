terraform {
  backend "gcs" {
    prefix = "nibas-events"
  }

#  required_providers {
#    postgresql = {
#      source = "cyrilgdn/postgresql"
#      version = "1.17.1"
#    }
#  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

locals {
  namespace = "nibas"
}

#variable "vault_addr" {}
#variable "vault_skip_verify" {}
#variable "GITHUB_TOKEN" {sensitive = true}

variable "nibas_events_version" {}
variable "ENVIRONMENT" {}

#provider "vault" {
#  address = var.vault_addr
#  skip_tls_verify = var.vault_skip_verify
#  auth_login {
#    path = "auth/jwt-dev/login"
#    method = "jwt"
#    parameters = {
#      role = "nibas-events-write-role"
#      jwt = var.GITHUB_TOKEN
#    }
#  }
#}
