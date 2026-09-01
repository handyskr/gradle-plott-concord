package kr.co.plott.concord

import kr.co.plott.concord.git.GitRepositoryFixture
import org.gradle.api.Action
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class ConcordPluginTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `registers the DSL and initializes its configured feature`() {
        GitRepositoryFixture(projectDirectory).init()
        val project = ProjectBuilder.builder().withProjectDir(projectDirectory.toFile()).build()
        ConcordPlugin().apply(project)

        project.extensions.getByType(ConcordExtension::class.java).gitHooks(
            Action { hooks ->
                hooks.hook("pre-commit", Action { spec -> spec.command("./gradlew test") })
            },
        )

        assertTrue(Files.isExecutable(projectDirectory.resolve(".git/hooks/pre-commit")))
    }
}
