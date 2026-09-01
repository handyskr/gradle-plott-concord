package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class GitHookNameTest {
    @Test
    fun `accepts client server and specialized hook names`() {
        val names = listOf("pre-commit", "pre-receive", "fsmonitor-watchman", "p4-pre-submit")

        assertEquals(names, names.map { GitHookName.of(it).value })
    }

    @Test
    fun `rejects unsafe or non-canonical names`() {
        listOf("", "../pre-commit", "hooks/pre-commit", "PreCommit", "pre_commit", ".sample").forEach { name ->
            assertThrows(InvalidGitHookException::class.java) {
                GitHookName.of(name)
            }
        }
    }
}
