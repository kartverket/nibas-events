# nibas-events

Spring Boot applikasjon som håndterer events i Nasjonal inndelingsbase.

# Innholdsfortegnelse

- [Bygge](#bygge)
- [Kjøre på lokal maskin](#lokal)
- [Formatering](#formatering)

# Bygge <a name="bygge"></a>
`./gradlew assemble`

# Kjøre på lokal maskin <a name="lokal"></a>
## Kjøre docker compose for å starte database og vault
```docker-compose -f docker-compose-local.yml up --remove-orphans```

## Definer Vault adresse og token
```export VAULT_ADDR=http://0.0.0.0:8200```
og
```export VAULT_TOKEN=myroot```

### Definer følgende på path /nibas/nibas-events-db-local i vault
```json
{
  "spring.datasource.password": "nibas",
  "spring.datasource.url": "jdbc:postgresql://localhost:5433/postgres",
  "spring.datasource.username": "postgres"
}
```
### Ønsker man teste endepunkter med sikkerhet, må det legges inn apiKeys på path /nibas/nibas-events-apikeys i Vault
```json
{
  "api.key.publisher": "<apiKey for å kunne bruke /v1/events/publiser>",
  "api.key.consumer": "<apiKey for å kunne bruke /v1/events>"
}
```

### Start applikasjon
```./gradlew bootRun --args='--spring.profiles.active=localhost'```

Alternativt starte fra intellij (NibasEventsApplication) og spesifisere environment variable: VAULT_TOKEN=<vault-token>, VAULT_ADDR=<vault-addr> samt sette active profile = localhost
 

# Hente docker image fra ghcr.io
`docker pull ghcr.io/kartverket/nibas-events:<versjonsnummer>`

# Starte docker image fra ghcr.io
`docker run -d --name nibas-events -p 8080:8080 ghcr.io/kartverket/nibas-events:<versjonsnummer>`

# Bygge lokalt docker image
```
./gradlew assemble
docker build --build-arg project_version_arg=0.0.1-SNAPSHOT . -t nibas-events:0.0.1-SNAPSHOT
```

# Starte lokalt docker image
`docker run -d --name nibas-events -p 8080:8080 nibas-events:0.0.1-SNAPSHOT`

# Ressurser
Gitt at <adresse-til-nibas-events> = localhost:8080
* Open API Specification: [http://<adresse-til-nibas>/api-docs](http://localhost:8080/api-docs)
* Swagger UI: [http://<adresse-til-nibas>/swagger-ui.html](http://localhost:8080/swagger-ui.html)

# Formatering <a name="formatering"></a>

Vi bruker EditorConfig for formatering (som IntelliJ default forstår).

# Opprette database i miljø
For nibas-events må vi (foreløpig) opprette applikasjonsbruker og database manuelt.
Root-bruker og connection-detaljer per miljø finnes i Vault under nøkkel `nibas-events-db-root`

Logg på med root-bruker på gjeldende database og kjør følgende sql-er:
```sql
CREATE ROLE nibas WITH LOGIN PASSWORD '<lag et sikkert passord>';
create database nibas with owner = nibas;
```

Opprett et innslag i Vault under nøkkel `nibas-events-db`. Dette skal ha følgende innhold:
```json
{
  "spring.datasource.password": "<password>",
  "spring.datasource.url": "jdbc:postgresql://<server>:<port>/nibas",
  "spring.datasource.username": "nibas"
}
```
