package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class GitHooksDirectoryResolverTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `resolves the default Git hooks directory`() {
        GitRepositoryFixture(projectDirectory).init()

        assertEquals(
            projectDirectory.resolve(".git/hooks"),
            resolver().resolve(),
        )
    }

    @Test
    fun `respects core hooksPath`() {
        val repository = GitRepositoryFixture(projectDirectory)
        repository.init()
        repository.config("core.hooksPath", ".project-hooks")

        assertEquals(projectDirectory.resolve(".project-hooks"), resolver().resolve())
    }

    @Test
    fun `rejects a directory outside a Git repository`() {
        assertThrows(InvalidGitHookException::class.java) {
            resolver().resolve()
        }
    }

    private fun resolver(): GitHooksDirectoryResolver {
        val project = ProjectBuilder.builder().withProjectDir(projectDirectory.toFile()).build()
        return GitHooksDirectoryResolver(projectDirectory, project.providers)
    }
}
