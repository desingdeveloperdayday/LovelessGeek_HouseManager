plugins {
    kotlin("jvm")
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
    implementation(kotlinx("coroutines-core", Version.coroutine))
    implementation(project(":shared"))

    testImplementation(kotlin("test-junit"))
}
