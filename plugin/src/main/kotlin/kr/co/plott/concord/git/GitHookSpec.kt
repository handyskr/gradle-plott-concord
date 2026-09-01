package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import java.nio.file.Path

open class GitHookSpec internal constructor(private val projectDirectory: Path) {
    private var configuredSource: GitHookSource? = null

    fun command(command: String) {
        configure(CommandHookSource(command))
    }

    fun file(relativePath: String) {
        if (relativePath.isBlank()) {
            throw InvalidGitHookException("Git hook file path must not be blank")
        }
        val declaredPath = Path.of(relativePath)
        if (declaredPath.isAbsolute) {
            throw InvalidGitHookException("Git hook file must be relative to the project: $relativePath")
        }
        val resolvedPath = projectDirectory.resolve(declaredPath).normalize()
        if (!resolvedPath.startsWith(projectDirectory)) {
            throw InvalidGitHookException("Git hook file escapes the project: $relativePath")
        }
        configure(FileHookSource(resolvedPath))
    }

    internal fun source(): GitHookSource =
        configuredSource ?: throw InvalidGitHookException("Git hook requires command(...) or file(...)")

    private fun configure(source: GitHookSource) {
        if (configuredSource != null) {
            throw InvalidGitHookException("Git hook accepts exactly one command(...) or file(...)")
        }
        configuredSource = source
    }
}
