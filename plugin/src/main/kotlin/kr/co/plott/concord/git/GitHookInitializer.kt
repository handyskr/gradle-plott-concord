package kr.co.plott.concord.git

import kr.co.plott.concord.Initializer
import kr.co.plott.concord.ManagedFileWriter
import kr.co.plott.concord.exception.ManagedFileConflictException
import java.nio.file.Files
import java.nio.file.Path

internal class GitHookInitializer(
    private val directoryResolver: GitHooksDirectoryResolver,
    private val contentResolver: GitHookContentResolver,
    private val writer: ManagedFileWriter,
    private val destinationTracker: HookDestinationTracker,
) : Initializer<GitHook> {
    override fun initialize(configuration: GitHook) {
        val hooksDirectory = directoryResolver.resolve()
        val destination = hooksDirectory.resolve(configuration.name.value)
        refuseSourceAsDestination(configuration, hooksDirectory, destination)
        val content = contentResolver.resolve(configuration.source)
        writer.write(destination, content, executable = true)
        destinationTracker.track(destination)
    }

    private fun refuseSourceAsDestination(configuration: GitHook, hooksDirectory: Path, destination: Path) {
        val source = configuration.source
        if (source !is FileHookSource || !isSameFile(source.path, destination)) {
            return
        }
        throw ManagedFileConflictException(
            "Git hook '${configuration.name.value}' declares its own destination as its source: " +
                "${source.path}. Git resolves the hooks directory of this repository to " +
                "$hooksDirectory, so nothing would be initialized anywhere else; check " +
                "core.hooksPath, or declare the source outside that directory.",
        )
    }

    private fun isSameFile(source: Path, destination: Path): Boolean {
        if (source.normalize() == destination.normalize()) {
            return true
        }
        if (!Files.exists(source) || !Files.exists(destination)) {
            return false
        }
        return Files.isSameFile(source, destination)
    }
}
