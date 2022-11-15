variable "topic" {}
variable "subscriber_name" {}
variable "iam_impersonate" {
  type    = bool
  default = false
}
variable "iam_kubernetes_sa" {
  default = ""
}

