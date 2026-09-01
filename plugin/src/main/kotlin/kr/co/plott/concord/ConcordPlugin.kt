package kr.co.plott.concord

import kr.co.plott.concord.git.CommandHookRenderer
import kr.co.plott.concord.git.GitHookContentResolver
import kr.co.plott.concord.git.GitHookInitializer
import kr.co.plott.concord.git.GitHooksDirectoryResolver
import kr.co.plott.concord.git.GitHooksExtension
import kr.co.plott.concord.git.HookFileReader
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConcordPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val projectDirectory = project.rootDir.toPath()
        val initializer = GitHookInitializer(
            directoryResolver = GitHooksDirectoryResolver(projectDirectory, project.providers),
            contentResolver = GitHookContentResolver(
                commandRenderer = CommandHookRenderer(),
                fileReader = HookFileReader(projectDirectory),
            ),
            writer = ManagedFileWriter(),
        )
        val gitHooks = GitHooksExtension(projectDirectory, initializer)
        project.extensions.create("concord", ConcordExtension::class.java, gitHooks)
    }
}
