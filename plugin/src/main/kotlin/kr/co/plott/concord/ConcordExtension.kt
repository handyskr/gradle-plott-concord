package kr.co.plott.concord

import kr.co.plott.concord.git.GitHooksExtension
import org.gradle.api.Action

open class ConcordExtension(private val gitHooks: GitHooksExtension) {
    fun gitHooks(action: Action<GitHooksExtension>) {
        action.execute(gitHooks)
    }
}
