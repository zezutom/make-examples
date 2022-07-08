val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val junit_version: String by project
val apache_http_client_version: String by project

plugins {
    id("java")
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:$apache_http_client_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}