package kr.co.plott.concord.git

import kr.co.plott.concord.Initializer
import kr.co.plott.concord.ManagedFileWriter

internal class GitHookInitializer(
    private val directoryResolver: GitHooksDirectoryResolver,
    private val contentResolver: GitHookContentResolver,
    private val writer: ManagedFileWriter,
) : Initializer<GitHook> {
    override fun initialize(configuration: GitHook) {
        val hooksDirectory = directoryResolver.resolve()
        val content = contentResolver.resolve(configuration.source)
        writer.write(hooksDirectory.resolve(configuration.name.value), content, executable = true)
    }
}
