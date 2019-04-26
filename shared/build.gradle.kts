plugins {
    kotlin("jvm") version "1.3.21"
}

repositories {
    jcenter()
    maven {
        url = uri("https://dl.bintray.com/pgutkowski/Maven")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api("com.google.code.gson:gson:2.8.5")
    api("com.github.pgutkowski:kgraphql:0.3.0")

    testImplementation(kotlin("test-junit"))
}
