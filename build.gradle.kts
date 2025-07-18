import java.time.Instant

plugins {
    `java-library`

    eclipse
    idea
}

applyCustomVersion()

tasks {
    jar {
        enabled = false
    }
}

subprojects {
    apply(plugin = "java-library")

    project.version = rootProject.version
    project.description = rootProject.description

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
//        maven("https://maven.devs.beer/") // ItemsAdder API Dependency
        maven("https://repo.nexomc.com/releases")
        maven("https://repo.oraxen.com/releases")
    }

    dependencies {
        compileOnly(rootProject.libs.annotations)
        annotationProcessor(rootProject.libs.annotations)
        compileOnly(rootProject.libs.paper)

        compileOnly(files("${rootProject.projectDir}/libs/api-itemsadder-4.0.10.jar"))
        compileOnly(rootProject.libs.nexo)
        compileOnly(rootProject.libs.oraxen)

        testImplementation(rootProject.libs.annotations)
        testImplementation(platform(rootProject.libs.junit.bom))
        testImplementation(rootProject.libs.bundles.junit)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        withJavadocJar()
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.compilerArgs.addAll(arrayListOf("-Xlint:all", "-Xlint:-processing", "-Xdiags:verbose"))
            options.release.set(21)
        }

        javadoc {
            isFailOnError = false
            val options = options as StandardJavadocDocletOptions
            options.encoding = Charsets.UTF_8.name()
            options.overview = "src/main/javadoc/overview.html"
            options.windowTitle = "${rootProject.name} Javadoc"
            options.tags("apiNote:a:API Note:", "implNote:a:Implementation Note:", "implSpec:a:Implementation Requirements:")
            options.addStringOption("Xdoclint:none", "-quiet")
            options.use()
        }

        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }

        test {
            useJUnitPlatform()
            failFast = false
        }
    }
}

fun applyCustomVersion() {
    // Apply custom version arg or append snapshot version
    val ver = properties["altVer"]?.toString() ?: "${rootProject.version}-SNAPSHOT.${Instant.now().epochSecond}"

    // Strip prefixed "v" from version tag
    rootProject.version = (if (ver.first().equals('v', true)) ver.substring(1) else ver.uppercase()).uppercase()
}