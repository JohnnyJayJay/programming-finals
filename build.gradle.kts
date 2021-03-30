import java.time.Duration
import java.time.temporal.ChronoUnit

plugins {
    java
    application
    checkstyle
    id("com.github.johnnyjayjay.codetester") version "0.2.0"
}



subprojects {

    group = "edu.kit.informatik"

    apply(plugin = "java")
    apply(plugin = "checkstyle")

    if (name != "common") {
        apply(plugin = "com.github.johnnyjayjay.codetester")
        apply(plugin = "application")

        codeTester {
            val codeTesterUser: String by project
            val codeTesterPassword: String by project
            username.set(codeTesterUser)
            password.set(codeTesterPassword)
            category.set("2020 WS Final " + (when (name) {
                "escape-networks" -> 1
                "fire-breaker" -> 2
                else -> throw AssertionError("Unknown project")
            }))
            readTimeout.set(Duration.of(5, ChronoUnit.MINUTES))
        }

        application {
            mainClass.set("edu.kit.informatik.${name.replace("-", "")}.program.Main")
        }

        tasks {
            register<DefaultTask>("finalize") {
                dependsOn(
                    build,
                    checkstyleMain,
                    codeTester
                )
            }

            zipSource {
                from(rootProject.project(":common").sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].allSource)
            }

            run.configure {
                standardInput = System.`in`
            }
        }

    }

    group = "edu.kit.informatik"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
            options.compilerArgs = listOf("-Xlint:all", "-Xlint:-processing", "-Xlint:-serial")
        }
    }

    checkstyle {
        configFile = configDirectory.file("requiredCheckstyleRules.xml").get().asFile
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}
