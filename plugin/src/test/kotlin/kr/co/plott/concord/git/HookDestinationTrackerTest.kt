package kr.co.plott.concord.git

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class HookDestinationTrackerTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `tracks an existing destination`() {
        val destination = Files.write(projectDirectory.resolve("pre-commit"), byteArrayOf(1, 2, 3))

        assertDoesNotThrow { tracker().track(destination) }
    }

    @Test
    fun `tracks a destination that does not exist`() {
        assertDoesNotThrow { tracker().track(projectDirectory.resolve("absent-hook")) }
    }

    private fun tracker(): HookDestinationTracker {
        val project = ProjectBuilder.builder().withProjectDir(projectDirectory.toFile()).build()
        return HookDestinationTracker(project.providers)
    }
}
