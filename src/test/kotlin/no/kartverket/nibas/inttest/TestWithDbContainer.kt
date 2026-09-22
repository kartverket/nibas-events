package no.kartverket.nibas.inttest

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.utility.DockerImageName
import org.testcontainers.postgresql.PostgreSQLContainer

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class TestWithDbContainer {

    companion object dbContainer : PostgreSQLContainer(
        DockerImageName.parse("postgis/postgis:14-3.2-alpine")
            .asCompatibleSubstituteFor("postgres"),
    ) {
        init {
            withDatabaseName("postgres")
            withUsername("nibas")
            withPassword("nibas")
            withCreateContainerCmdModifier { cmd -> cmd.withName("nibas-test-container") }
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", dbContainer::getJdbcUrl)
            registry.add("spring.datasource.username", dbContainer::getUsername)
            registry.add("spring.datasource.password", dbContainer::getPassword)
        }
    }
}
