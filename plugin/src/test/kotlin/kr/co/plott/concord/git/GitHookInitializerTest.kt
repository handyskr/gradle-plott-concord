package kr.co.plott.concord.git

import kr.co.plott.concord.ManagedFileWriter
import kr.co.plott.concord.exception.ManagedFileConflictException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class GitHookInitializerTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `initializes command hooks in the repository hook path`() {
        GitRepositoryFixture(projectDirectory).init()

        initializer().initialize(
            GitHook(GitHookName.of("pre-commit"), CommandHookSource("./gradlew test")),
        )

        val hook = projectDirectory.resolve(".git/hooks/pre-commit")
        assertTrue(Files.readString(hook).contains("if ! ./gradlew test; then"))
        assertTrue(Files.isExecutable(hook))
    }

    @Test
    fun `initializes file hooks without changing their bytes`() {
        GitRepositoryFixture(projectDirectory).init()
        val content = byteArrayOf(0, 10, 20, 30)
        val source = Files.write(projectDirectory.resolve("commit-msg"), content)

        initializer().initialize(
            GitHook(GitHookName.of("commit-msg"), FileHookSource(source)),
        )

        assertArrayEquals(content, Files.readAllBytes(projectDirectory.resolve(".git/hooks/commit-msg")))
    }

    @Test
    fun `refuses a file source that is its own destination`() {
        val fixture = GitRepositoryFixture(projectDirectory)
        fixture.init()
        fixture.config("core.hooksPath", ".githooks")
        val source = projectDirectory.resolve(".githooks/pre-commit")
        Files.createDirectories(source.parent)
        Files.write(source, byteArrayOf(1, 2, 3))

        val failure = assertThrows(ManagedFileConflictException::class.java) {
            initializer().initialize(GitHook(GitHookName.of("pre-commit"), FileHookSource(source)))
        }

        assertTrue(failure.message!!.contains("declares its own destination as its source"))
        assertTrue(failure.message!!.contains("core.hooksPath"))
        assertFalse(Files.exists(projectDirectory.resolve(".githooks/.plott-concord")))
    }

    private fun initializer(): GitHookInitializer {
        val providers = ProjectBuilder.builder().withProjectDir(projectDirectory.toFile()).build().providers
        return GitHookInitializer(
            directoryResolver = GitHooksDirectoryResolver(projectDirectory, providers),
            contentResolver = GitHookContentResolver(
                commandRenderer = CommandHookRenderer(),
                fileReader = HookFileReader(projectDirectory),
            ),
            writer = ManagedFileWriter(),
        )
    }
}
