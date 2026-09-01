package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class GitHookSpecTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `builds a command source`() {
        val spec = GitHookSpec(projectDirectory)
        spec.command("./gradlew test")

        assertEquals("./gradlew test", assertInstanceOf(CommandHookSource::class.java, spec.source()).command)
    }

    @Test
    fun `resolves a project relative file source`() {
        val spec = GitHookSpec(projectDirectory)
        spec.file("hooks/pre-commit")

        assertEquals(
            projectDirectory.resolve("hooks/pre-commit"),
            assertInstanceOf(FileHookSource::class.java, spec.source()).path,
        )
    }

    @Test
    fun `requires exactly one source`() {
        val missing = GitHookSpec(projectDirectory)
        assertThrows(InvalidGitHookException::class.java) { missing.source() }

        val duplicate = GitHookSpec(projectDirectory)
        duplicate.command("first")
        assertThrows(InvalidGitHookException::class.java) { duplicate.file("hooks/second") }
    }

    @Test
    fun `rejects blank absolute and escaping file paths`() {
        listOf("", projectDirectory.resolve("hook").toString(), "../hook").forEach { path ->
            assertThrows(InvalidGitHookException::class.java) {
                GitHookSpec(projectDirectory).file(path)
            }
        }
    }
}
