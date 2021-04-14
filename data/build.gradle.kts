val exposedVersion: String by project
val h2Version: String by project

plugins {
}

group = "com.shouman.reseller"
version = "0.0.1"

dependencies {

    implementation(project(":domain"))

    // Database
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
}



