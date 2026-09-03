package kr.co.plott.concord.git

import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

/**
 * Declares an initialized hook as an input of the configuration that initialized it.
 *
 * Initialization runs while Gradle configures the project, so a build that reuses the
 * configuration cache would otherwise never look at the hooks again: a hook deleted after the
 * entry was stored would stay missing, silently, until something else invalidated the entry.
 * Gradle re-evaluates value sources when it decides whether a cached entry is still usable, so
 * reading the destination through one makes its disappearance or replacement invalidate that
 * entry and initialize the hook again.
 */
internal class HookDestinationTracker(private val providers: ProviderFactory) {
    fun track(destination: Path) {
        providers.of(HookDestinationState::class.java) { source ->
            source.parameters.destination.set(destination.toString())
        }.get()
    }
}

internal abstract class HookDestinationState : ValueSource<String, HookDestinationState.Parameters> {
    internal interface Parameters : ValueSourceParameters {
        val destination: Property<String>
    }

    override fun obtain(): String {
        val destination = Path.of(parameters.destination.get())
        if (!Files.isRegularFile(destination)) {
            return ABSENT
        }
        val digest = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(destination))
        return digest.joinToString("") { byte -> "%02x".format(byte) }
    }

    private companion object {
        const val ABSENT = "absent"
    }
}
