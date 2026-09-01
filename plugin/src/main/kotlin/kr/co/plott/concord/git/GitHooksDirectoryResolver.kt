package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.gradle.api.provider.ProviderFactory
import java.nio.file.Path

internal class GitHooksDirectoryResolver(
    private val projectDirectory: Path,
    private val providers: ProviderFactory,
) {
    fun resolve(): Path {
        val output = providers.exec { spec ->
            spec.workingDir(projectDirectory.toFile())
            spec.commandLine("git", "rev-parse", "--git-path", "hooks")
            spec.isIgnoreExitValue = true
        }
        val result = output.result.get()
        if (result.exitValue != 0) {
            val error = output.standardError.asText.get().trim()
            throw InvalidGitHookException(
                "Git hooks require a repository at $projectDirectory${errorSuffix(error)}",
            )
        }
        val declaredPath = output.standardOutput.asText.get().trim()
        if (declaredPath.isEmpty()) {
            throw InvalidGitHookException("Git returned an empty hooks path for $projectDirectory")
        }
        return projectDirectory.resolve(declaredPath).normalize()
    }

    private fun errorSuffix(error: String): String = if (error.isEmpty()) "" else ": $error"
}
