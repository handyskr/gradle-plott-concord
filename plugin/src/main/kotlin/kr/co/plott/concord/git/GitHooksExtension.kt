package kr.co.plott.concord.git

import kr.co.plott.concord.Initializer
import kr.co.plott.concord.exception.InvalidGitHookException
import org.gradle.api.Action
import java.nio.file.Path

open class GitHooksExtension internal constructor(
    private val projectDirectory: Path,
    private val initializer: Initializer<GitHook>,
) {
    private val configuredHooks = mutableSetOf<GitHookName>()

    fun hook(name: String, action: Action<GitHookSpec>) {
        val hookName = GitHookName.of(name)
        if (!configuredHooks.add(hookName)) {
            throw InvalidGitHookException("Git hook '$name' is configured more than once")
        }
        val spec = GitHookSpec(projectDirectory)
        action.execute(spec)
        initializer.initialize(GitHook(hookName, spec.source()))
    }
}
