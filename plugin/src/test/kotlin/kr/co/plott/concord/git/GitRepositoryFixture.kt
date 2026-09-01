package kr.co.plott.concord.git

import java.nio.file.Path

internal class GitRepositoryFixture(private val directory: Path) {
    fun init() {
        git("init", "--quiet")
    }

    fun config(key: String, value: String) {
        git("config", key, value)
    }

    private fun git(vararg arguments: String) {
        val process = ProcessBuilder(listOf("git", "-C", directory.toString()) + arguments)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        check(process.waitFor() == 0) { "Git command failed: $output" }
    }
}
