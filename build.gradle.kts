plugins {
    kotlin("jvm") version "2.5.0-Beta2"
}

group = "com.mvukic.aoc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(25)
}