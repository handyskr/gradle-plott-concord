package kr.co.plott.concord.git

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class GitHookContentResolverTest {
    @field:TempDir
    lateinit var projectDirectory: Path

    @Test
    fun `selects command rendering`() {
        val resolver = resolver()

        assertTrue(resolver.resolve(CommandHookSource("./gradlew test")).decodeToString().contains("./gradlew test"))
    }

    @Test
    fun `selects exact file content`() {
        val content = byteArrayOf(10, 20, 30)
        val source = Files.write(projectDirectory.resolve("hook"), content)

        assertArrayEquals(content, resolver().resolve(FileHookSource(source)))
    }

    private fun resolver() = GitHookContentResolver(CommandHookRenderer(), HookFileReader(projectDirectory))
}
