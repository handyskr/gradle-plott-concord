package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CommandHookSourceTest {
    @Test
    fun `keeps the declared command`() {
        assertEquals("./gradlew test", CommandHookSource("./gradlew test").command)
    }

    @Test
    fun `rejects a blank command`() {
        assertThrows(InvalidGitHookException::class.java) {
            CommandHookSource("  ")
        }
    }
}
