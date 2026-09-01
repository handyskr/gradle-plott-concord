package kr.co.plott.concord.git

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class CommandHookRendererTest {
    @field:TempDir
    lateinit var directory: Path

    @Test
    fun `renders an executable shell contract that never blocks Git`() {
        val hook = directory.resolve("hook")
        Files.write(hook, CommandHookRenderer().render("false"))

        val process = ProcessBuilder("/bin/sh", hook.toString())
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()

        assertEquals(0, process.waitFor())
        assertTrue(output.contains("Git operation will continue"))
        assertTrue(Files.readString(hook).endsWith("\n"))
    }
}
