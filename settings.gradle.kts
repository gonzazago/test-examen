plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
// test-examen/settings.gradle.kts
rootProject.name = "test-examen"
include("container-service")
include("orders-service")
include("orchestator-app")
