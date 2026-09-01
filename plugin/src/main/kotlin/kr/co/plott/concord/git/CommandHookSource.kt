package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException

data class CommandHookSource(val command: String) : GitHookSource {
    init {
        if (command.isBlank()) {
            throw InvalidGitHookException("Git hook command must not be blank")
        }
    }
}
