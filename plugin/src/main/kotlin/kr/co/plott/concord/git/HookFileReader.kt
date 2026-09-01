package kr.co.plott.concord.git

import kr.co.plott.concord.exception.InvalidGitHookException
import java.nio.file.Files
import java.nio.file.Path

internal class HookFileReader(private val projectDirectory: Path) {
    fun read(path: Path): ByteArray {
        if (Files.isSymbolicLink(path)) {
            throw InvalidGitHookException("Git hook source file must not be a symbolic link: $path")
        }
        if (!Files.isRegularFile(path)) {
            throw InvalidGitHookException("Git hook source file does not exist: $path")
        }
        val realProjectDirectory = projectDirectory.toRealPath()
        val realPath = path.toRealPath()
        if (!realPath.startsWith(realProjectDirectory)) {
            throw InvalidGitHookException("Git hook source file escapes the project: $path")
        }
        return Files.readAllBytes(realPath)
    }
}
