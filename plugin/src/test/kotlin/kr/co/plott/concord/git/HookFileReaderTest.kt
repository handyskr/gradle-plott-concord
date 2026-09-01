package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class HookFileReaderTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `reads file bytes without rewriting them`() {
        val content = byteArrayOf(0, 1, 2, 127)
        val source = Files.write(projectDirectory.resolve("hook"), content)

        assertArrayEquals(content, HookFileReader(projectDirectory).read(source))
    }

    @Test
    fun `rejects a missing source`() {
        assertThrows(InvalidGitHookException::class.java) {
            HookFileReader(projectDirectory).read(projectDirectory.resolve("missing"))
        }
    }

    @Test
    fun `rejects a symbolic source`() {
        assumeFalse(System.getProperty("os.name").startsWith("Windows"))
        val source = Files.writeString(projectDirectory.resolve("source"), "hook")
        val link = Files.createSymbolicLink(projectDirectory.resolve("link"), source.fileName)

        assertThrows(InvalidGitHookException::class.java) {
            HookFileReader(projectDirectory).read(link)
        }
    }

    @Test
    fun `rejects a source outside the project`() {
        val project = Files.createDirectory(projectDirectory.resolve("project"))
        val outside = Files.writeString(projectDirectory.resolve("outside"), "hook")

        assertThrows(InvalidGitHookException::class.java) {
            HookFileReader(project).read(outside)
        }
    }
}
