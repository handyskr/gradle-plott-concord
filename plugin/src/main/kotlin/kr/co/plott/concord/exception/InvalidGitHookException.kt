package kr.co.plott.concord.exception

class InvalidGitHookException(message: String, cause: Throwable? = null) :
    InvalidConfigurationException(message, cause)
