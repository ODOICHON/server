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
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // Database
    runtimeOnly("com.mysql:mysql-connector-j")
    // Jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    // Jasypt
    implementation ("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
    // CoolSms
    implementation("net.nurigo:javaSDK:2.2")
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
}

val asciidoctorExt: Configuration by configurations.creating
dependencies {
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

val snippetsDir by extra { file("build/generated-snippets")}
tasks {
    test {
        extensions.configure(JacocoTaskExtension::class) {
            destinationFile = file("$buildDir/jacoco/jacoco.exec")
        }
        finalizedBy(jacocoTestReport)

        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        dependsOn(test)
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }
    build {
        dependsOn(asciidoctor)
    }
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
        html.isEnabled = true
        xml.isEnabled = false
        csv.isEnabled = false
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
//        rule {
//            limit {
//                minimum = "0.0".toBigDecimal()
//            }
//
//        }

        rule {
//            enabled = true
//
//            element = "CLASS"
//
//            limit{
//                counter = "BRANCH"
//                value = "COVEREDRATIO"
//                minimum = "0.0".toBigDecimal()
//            }



            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "800.0".toBigDecimal()

            }

            excludes = listOf(

            )
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "RUns the unit tests with coverage"

    dependsOn(":test",
            ":jacocoTestReport",
            ":jacocoTestCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}