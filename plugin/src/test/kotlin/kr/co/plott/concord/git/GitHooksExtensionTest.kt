package kr.co.plott.concord.git

import kr.co.plott.concord.Initializer
import kr.co.plott.concord.exception.InvalidGitHookException
import org.gradle.api.Action
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class GitHooksExtensionTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `passes any valid hook and command configuration to the initializer`() {
        val initialized = mutableListOf<GitHook>()
        val extension = GitHooksExtension(projectDirectory, Initializer { initialized += it })

        extension.hook("post-index-change", Action { it.command("./gradlew test") })

        assertEquals("post-index-change", initialized.single().name.value)
        assertEquals(
            "./gradlew test",
            assertInstanceOf(CommandHookSource::class.java, initialized.single().source).command,
        )
    }

    @Test
    fun `does not initialize an incomplete hook`() {
        val extension = GitHooksExtension(projectDirectory, Initializer { error("must not initialize") })

        assertThrows(InvalidGitHookException::class.java) {
            extension.hook("pre-commit", Action {})
        }
    }

    @Test
    fun `rejects a duplicate hook declaration`() {
        val extension = GitHooksExtension(projectDirectory, Initializer {})
        extension.hook("pre-commit", Action { it.command("first") })

        assertThrows(InvalidGitHookException::class.java) {
            extension.hook("pre-commit", Action { it.command("second") })
        }
    }
}
