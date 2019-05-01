plugins {
    kotlin("jvm") version Version.kotlin
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
    implementation(kotlinx("coroutines-core", Version.coroutine))
    implementation(project(":shared"))
    implementation(project(":server:data"))

    api("com.github.pgutkowski:kgraphql:${Version.kgraphql}")

    testImplementation(kotlin("test-junit"))
}
