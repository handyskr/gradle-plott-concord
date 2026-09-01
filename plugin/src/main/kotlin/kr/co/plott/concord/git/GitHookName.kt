package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException

@JvmInline
value class GitHookName private constructor(val value: String) {
    companion object {
        private val SUPPORTED_NAMES = setOf(
            "applypatch-msg",
            "pre-applypatch",
            "post-applypatch",
            "pre-commit",
            "pre-merge-commit",
            "prepare-commit-msg",
            "commit-msg",
            "post-commit",
            "pre-rebase",
            "post-checkout",
            "post-merge",
            "pre-push",
            "pre-receive",
            "update",
            "proc-receive",
            "post-receive",
            "post-update",
            "reference-transaction",
            "push-to-checkout",
            "pre-auto-gc",
            "post-rewrite",
            "sendemail-validate",
            "fsmonitor-watchman",
            "p4-changelist",
            "p4-prepare-changelist",
            "p4-post-changelist",
            "p4-pre-submit",
            "post-index-change",
        )

        fun of(value: String): GitHookName {
            if (value !in SUPPORTED_NAMES) {
                throw InvalidGitHookException(
                    "Unsupported Git hook name '$value'",
                )
            }
            return GitHookName(value)
        }
    }
}
