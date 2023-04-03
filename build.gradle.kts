import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.sonarqube") version "3.5.0.2730"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.allopen") version "1.4.32"
    kotlin("kapt") version "1.7.10"
    jacoco
}

sonarqube {
    properties {
        property ("sonar.projectKey", "ODOICHON_server")
        property ("sonar.organization", "odoichon")
        property ("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sources", "src")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.test.inclusions", "**/*Test.kt")
        property("sonar.exclusions", "**/test/**, **/resources/**, **/docs/**, **/*Application*.kt, **/dto/**, **/*Exception*.kt, **/*ErrorCode*.kt, **/*Category*.kt" )
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/html/jacocoTestReport.xml")
    }
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
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
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
    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

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
        html.destination = file("$buildDir/reports/jhouse-report.html")
        csv.isEnabled = true
        xml.isEnabled = true
    }

    var excludes = mutableListOf<String>()
    excludes.add("**/global/**")
    excludes.add("**/domain/**/dto/**")
    excludes.add("**/domain/**/entity/**")
    excludes.add("**/JhouseServerApplicationKt*")
    excludes.add("com/example/jhouse_server/admin/**")
    excludes.add("**/*Converter*.kt")
    excludes.add("**/resources/**")
    excludes.add("**/build/generated/source/**")
    excludes.add("**/*RepositoryImpl.kt")

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludes)
        }
    )

    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true // rule을 on/off
            element = "CLASS" // class 단위로 rule 체크
            limit { // 라인 커버리지 최소 80% 충족
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }
            limit {// 빈 줄 제외한 코드 라인수 최대 1000라인으로 제한한다.
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "550.0".toBigDecimal()
            }
        }
    }
    var excludes = mutableListOf<String>()
    excludes.add("com/example/jhouse_server/global/**")
    excludes.add("com/example/jhouse_server/domain/**/dto/**")
    excludes.add("com/example/jhouse_server/domain/**/entity/**")
    excludes.add("**/JhouseServerApplicationKt*")
    excludes.add("com/example/jhouse_server/admin/user/**")
    excludes.add("com/example/jhouse_server/domain/board/entity/BoardCategoryConverter.kt")
    excludes.add("com/example/jhouse_server/domain/board/entity/PrefixCategoryConverter.kt")
    excludes.add("**/resources/**")

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludes)
        }
    )
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(":test",
            ":jacocoTestReport",
            ":jacocoTestCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}