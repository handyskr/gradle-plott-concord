package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException

@JvmInline
value class GitHookName private constructor(val value: String) {
    companion object {
        private val VALID_NAME = Regex("[a-z][a-z0-9-]*")

        fun of(value: String): GitHookName {
            if (!VALID_NAME.matches(value)) {
                throw InvalidGitHookException(
                    "Invalid Git hook name '$value': use lowercase letters, digits, and hyphens",
                )
            }
            return GitHookName(value)
        }
    }
}
