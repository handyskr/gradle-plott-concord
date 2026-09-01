# Plott Concord

Plott Concord initializes project development hooks and settings from Gradle configuration. Integrations are added as explicit features instead of being expressed through a generic file manifest.

## Why this name

“Concord” describes several execution environments agreeing on one project-owned definition without naming or privileging any one environment. The public plugin ID is `kr.co.plott.concord`; the repository is `handyskr/gradle-plott-concord`.

## Apply the plugin

Apply the plugin from the Gradle Plugin Portal. Consumers do not need repository credentials:

```kotlin
plugins {
    id("kr.co.plott.concord") version "VERSION"
}

concord {
    gitHooks {
        hook("pre-commit") {
            command("./gradlew test")
        }
        hook("commit-msg") {
            file("hooks/commit-msg")
        }
    }
}
```

The DSL call initializes the hook while Gradle configures the project. It does not add an installer dependency to every task. Reusing the configuration cache performs no additional initialization; changing the build configuration causes Gradle to configure and initialize it again.

## Feature 1: Git hooks

`hook(name)` accepts any safe Git hook name, so client-side, server-side, and future Git hook names do not require a plugin release. Names are limited to lowercase letters, digits, and hyphens to prevent path traversal.

- `command(...)` generates a shell wrapper. It reports command failures but exits successfully, so it does not block the Git operation.
- `file(...)` copies the project-relative file byte-for-byte and preserves that file's own exit behavior.

Concord tracks managed content with SHA-256 state next to the hooks directory. It updates unchanged managed hooks but refuses unmanaged files, symbolic links, or locally modified managed hooks. Hook directory discovery uses Git itself and therefore respects worktrees and `core.hooksPath`.

## Sample build

The `sample/` module is a real Java/JUnit project. It applies the plugin, configures command- and file-backed hooks, and uses the standard `test` task without sample-specific lifecycle task names. Plugin implementation tests remain in the single `plugin/src/test` source set and use JUnit Jupiter directly.

The root package contains shared contracts and utilities such as `Initializer<T>` and managed-file writing. Git-specific DSL and behavior live under `kr.co.plott.concord.git`; shared errors live under `kr.co.plott.concord.exception`. JaCoCo generates XML and HTML coverage reports after the plugin test suite without imposing an arbitrary coverage gate.

## Release tags and versions

Release tags must use `vMAJOR.MINOR.PATCH`, for example `v0.1.0`. Numeric components cannot contain leading zeroes, and pre-release suffixes are intentionally not accepted yet.

Every pushed tag is checked by `.github/workflows/validate-tag.yml`. The publish workflow uses the same resolver and automatically converts the release tag to the plugin version by removing the leading `v`. A repository workflow cannot reject the tag before GitHub receives it; enforcement beyond a failing validation check requires a repository ruleset that requires the validation workflow.

## Publish

The `plugin` build uses Gradle's official Plugin Publish Plugin. A published release or manual workflow run validates the tag, checks the plugin, and invokes `publishPlugins` with `GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET` from GitHub Actions secrets. These credentials are required only for publishing; consuming the public plugin is anonymous.
