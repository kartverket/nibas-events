# nibas-events

Spring Boot applikasjon som håndterer events i Nasjonal inndelingsbase.

# Innholdsfortegnelse

- [Bygge](#bygge)
- [Kjøre på lokal maskin](#lokal)
- [Formatering](#formatering)

# Bygge <a name="bygge"></a>
`./gradlew assemble`

# Kjøre på lokal maskin <a name="lokal"></a>
```
# Autentisering mot github container registry
For å pulle database-image må man ha autentisert seg mot github container registry: https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry#authenticating-to-the-container-registry

# Start database (dersom du sitter på windows må du spesifisere -e PGDATA=<vilkårlig folder-navn>, ellers klages det på at den ikke kan kjøre chmod på default-mapper) 
docker run -d --name nibas-events-db -p 5433:5432 -e NIBAS_USER_PW=<sjekk-vault> -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=nibas ghcr.io/kartverket/nibas-db:v<siste versjonsnummer>

#Start applikasjon
./gradlew bootRun 
Alternativt starte fra intellij (NibasEventsApplication) og spesifisere environment variable: VAULT_TOKEN=<vault-token>, samt sette active profile = localhost
``` 

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

Vi bruker EditorConfig for formatering (som IntelliJ default forstår). I tillegg benyttes Detekt i byggeløypa (anbefalt av SKIP). Dette kan en også sette opp i
IntelliJ for å få varsler der. Gå til File->Settings->Plugins og installer Detekt-plugin. Etterpå gå til File->Settings->Tools->Detekt. Huk av Enable Detekt,
Enable rules og Enable formatting. Under Configuration Files, legg til stien til detekt.yml som ligger på rota.

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
  "spring.flyway.url": "jdbc:postgresql://<server>:<port>/nibas",
  "spring.r2dbc.password": "<password>",
  "spring.r2dbc.url": "r2dbc:postgresql://<server>:<port>/nibas",
  "spring.r2dbc.username": "nibas"
}
```
