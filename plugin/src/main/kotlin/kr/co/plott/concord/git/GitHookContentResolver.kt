package kr.co.plott.concord.git

internal class GitHookContentResolver(
    private val commandRenderer: CommandHookRenderer,
    private val fileReader: HookFileReader,
) {
    fun resolve(source: GitHookSource): ByteArray = when (source) {
        is CommandHookSource -> commandRenderer.render(source.command)
        is FileHookSource -> fileReader.read(source.path)
    }
}
