package kr.co.plott.concord.exception

import org.gradle.api.GradleException

open class ConcordException(message: String, cause: Throwable? = null) : GradleException(message, cause)
