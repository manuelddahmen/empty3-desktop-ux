import org.gradle.initialization.Environment

plugins {
    id("java")
    kotlin("jvm") version "2.4.0"
    id("application") // enabling the plugin here
}
val JAVA_VERSION = 25
group = "one.empty3"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(JAVA_VERSION)
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        kotlin {
            srcDirs("src/main/kotlin", "src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
        resources {
            srcDirs("src/test/resources")
        }
    }
}


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
    // Empty3 libraries
    implementation("one.empty3.libs:partial-desktop:0.0.39-17")
    implementation("one.empty3.libs:commons-mp:0.0.17-17")
    implementation("one.empty3:empty3-library-mp:2026.6.7")
 }

tasks.test {
    useJUnitPlatform()
}
tasks.register<JavaExec>("runClass") {
    group = "application"
    description = "Run a main class: ./gradlew runClass -P className=my.package.MyClass. Try also env variable : MAIN_CLASS_NAME"
    var className = project.findProperty("className")?.toString()
    if(className==null) {
        println("className is null.\nSyntax : ./gradlew runClass -P className=my.package.MyClass")
    }
    var mainClassName = className?:ProcessBuilder().environment().getOrDefault("MAIN_CLASS_NAME", null)

    if (mainClassName != null) {
        println("Main class name from environment variable: $mainClassName")
    } else {
        println("Main class name from environment variable: (NOT FOUND)\nMAIN_CLASS_NAME=$mainClassName")
        mainClassName = "one.empty3.HelloWorld"
    }
    mainClass.set(mainClassName)
    classpath = sourceSets.main.get().runtimeClasspath
    (project.findProperty("args") as? String)?.split(' ')?.let { args(it)
    }
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