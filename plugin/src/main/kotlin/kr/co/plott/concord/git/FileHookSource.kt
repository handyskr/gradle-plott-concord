package kr.co.plott.concord.git

import java.nio.file.Path

data class FileHookSource(val path: Path) : GitHookSource
