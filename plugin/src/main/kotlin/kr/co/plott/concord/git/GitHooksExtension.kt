package kr.co.plott.concord.git

import kr.co.plott.concord.Initializer
import org.gradle.api.Action
import java.nio.file.Path

open class GitHooksExtension internal constructor(
    private val projectDirectory: Path,
    private val initializer: Initializer<GitHook>,
) {
    fun hook(name: String, action: Action<GitHookSpec>) {
        val spec = GitHookSpec(projectDirectory)
        action.execute(spec)
        initializer.initialize(GitHook(GitHookName.of(name), spec.source()))
    }
}
