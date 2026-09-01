package kr.co.plott.concord.git

data class GitHook(
    val name: GitHookName,
    val source: GitHookSource,
)
