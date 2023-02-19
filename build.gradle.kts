import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.allopen") version "1.4.32"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // Springboot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // Database
    runtimeOnly("com.mysql:mysql-connector-j")

    // Jasypt
    implementation ("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
    // Springboot Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

//val asciidoctorExt: Configuration by configurations.creating
//dependencies {
//    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
//}

val snippetsDir by extra { file("build/generated-snippets")}
tasks {

    clean {
        delete("src/main/resources/static/docs")
    }

    test {
        useJUnitPlatform()
        systemProperty("org.springframework.restdocs.outputDir", snippetsDir)
        outputs.dir(snippetsDir)
    }

    build {
        dependsOn("copyDocument")
    }

    asciidoctor {
        dependsOn(test)
        attributes (
            mapOf("snippets" to snippetsDir)
            )
        inputs.dir(snippetsDir)

        doFirst{
            delete("src/main/resources/static/docs")
        }
    }

    register<Copy>("copyDocument") {
        dependsOn(asciidoctor)

        destinationDir = file(".")
        from(asciidoctor.get().outputDir) {
            into("src/main/resources/static/docs")
        }
    }
    bootJar {
        dependsOn(asciidoctor)

        from(asciidoctor.get().outputDir) {
            into("BOOT-INF/classes/static/docs")
        }
    }
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
    classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                exclude(
                        "**/global/**",
                        "**/domain/*/dto/*",
                        "**/domain/*/entity/*"
                )
            }
    )
    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {

        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()

            }

            excludes = listOf(

            )
        }
    }

    classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching{
                exclude(
                        "**/global/**"
                )
            }
    )
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "RUns the unit tests with coverage"

    dependsOn(
            ":test",
            ":jacocoTestReport",
            ":jacocoTestCoverageVerification"
    )


    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}