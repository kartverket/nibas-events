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
# Start database
docker run -d --name nibas-events-db -p 5433:5432 -e NIBAS_USER_PW=<sjekk-vault> -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=nibas ghcr.io/kartverket/nibas-db:v0.0.81
./gradlew bootRun 
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
