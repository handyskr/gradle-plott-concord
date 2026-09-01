plugins {
    java
    id("kr.co.plott.concord")
}

concord {
    gitHooks {
        hook("pre-commit") {
            command("./gradlew test")
        }
        hook("commit-msg") {
            file("sample/hooks/commit-msg")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
