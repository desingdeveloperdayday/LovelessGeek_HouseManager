plugins {
    kotlin("jvm") version "1.3.21"
    id("aws.sam") version "0.1.0"
}

repositories {
    jcenter()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":shared"))
    implementation("com.amazonaws:aws-lambda-java-core:1.2.0")
    implementation("com.amazonaws:aws-lambda-java-events:2.2.5")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.0.0")
    testImplementation(kotlin("test-junit"))
}

sam {
    template = file("template.yml")
    bucket = "lovelessgeek-house-manager-api-dev"
    stack = "lovelessgeek-house-manager-api-dev"
}