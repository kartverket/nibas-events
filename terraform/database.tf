resource "random_password" "nibas-events-db-app-pw" {
  length  = 29
  special = true
  lower   = true
  upper   = true
  numeric  = true
}

data "vault_generic_secret" "nibas-events-db-root" {
  path = "nibas/nibas-events-db-root"
}

provider "postgresql" {
  host = data.vault_generic_secret.nibas-events-db-root.data["host"]
  port = data.vault_generic_secret.nibas-events-db-root.data["port"]
  username = data.vault_generic_secret.nibas-events-db-root.data["username"]
  password = data.vault_generic_secret.nibas-events-db-root.data["password"]
}

resource "postgresql_role" "nibas-events-pg-role" {
  name = "nibas-events"
  login = true
  password = random_password.nibas-events-db-app-pw.result
}

resource "postgresql_database" "nibas-events-pg-db" {
  name = "nibas-events"
  owner = postgresql_role.nibas-events-pg-role.name
}

resource "vault_generic_secret" "nibas-events-db-app-pw-vault" {
  path = "nibas/nibas-events-db"

  data_json = <<EOT
{
  "spring.r2dbc.username":   "${postgresql_role.nibas-events-pg-role.name}",
  "spring.r2dbc.password":   "${random_password.nibas-events-db-app-pw.result}",
  "spring.r2dbc.url": "r2dbc:postgresql://${data.vault_generic_secret.nibas-events-db-root.data["host"]}:${data.vault_generic_secret.nibas-events-db-root.data["port"]}/${postgresql_database.nibas-events-pg-db.name}",
  "spring.flyway.url": "jdbc:postgresql://${data.vault_generic_secret.nibas-events-db-root.data["host"]}:${data.vault_generic_secret.nibas-events-db-root.data["port"]}/${postgresql_database.nibas-events-pg-db.name}"
}
EOT
}
