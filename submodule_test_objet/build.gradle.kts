plugins {
    id("java")
    id("application") // enabling the plugin here
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven("https://www.jetbrains.com/intellij-repository/releases")
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("one.empty3:empty3-desktop-ux:2026.6.4-121")
}

tasks.test {
    useJUnitPlatform()
}
tasks.register<JavaExec>("runClass") {
    group = "application"
    description = "Run a main class: ./gradlew runClass -PclassName=my.package.MyClass"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(project.findProperty("className")?.toString() ?: "one.empty3.HelloWorld")
    (project.findProperty("args") as? String)?.split(' ')?.let { args(it) }
}

tasks.register<Exec>("buildDockerImage") {
    group = "docker"
    description = "Builds the Docker image for the project"
    workingDir = project.rootDir
    commandLine("docker", "build", "-t", "submodule-test-objet", "-f", "submodule_test_objet/Dockerfile", ".")
}

tasks.register<Exec>("runDockerImage") {
    group = "docker"
    description = "Runs the Docker image"
    dependsOn("buildDockerImage")
    commandLine("docker", "run", "--rm", "submodule-test-objet")
}