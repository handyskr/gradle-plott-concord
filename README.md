# Plott Concord

Plott Concord initializes repository development hooks and settings from declarative Gradle configuration. Features have explicit DSLs; projects do not maintain a separate generic manifest.

> **Publication status:** `kr.co.plott.concord:0.1.0` was submitted successfully and is awaiting the Gradle Plugin Portal's initial manual approval. The plugin will not resolve from the Portal until that review is complete.

The name “Concord” represents multiple development tools agreeing on one project-owned configuration. The public plugin ID is `kr.co.plott.concord`, and the implementation coordinate is `kr.co.plott:gradle-plott-concord`.

## Requirements

- A Git working tree for the Git hooks feature
- A Gradle build using Kotlin or Groovy DSL
- A shell-compatible environment for command-backed hooks

The current release is built with Gradle 9.7.1. CI validates the plugin and sample with Java 21. Configuration-cache storage and reuse are covered by the sample build, but a wider Gradle compatibility matrix has not yet been claimed.

## Apply the plugin

After Plugin Portal approval, consumers can apply the public version without repository credentials:

```kotlin
plugins {
    id("kr.co.plott.concord") version "0.1.0"
}
```

During repository development, this project resolves the plugin from the included `plugin/` build.

## Configure Git hooks

```kotlin
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

- `command(...)` generates a non-blocking shell wrapper. Failures are reported, but the wrapper exits successfully.
- `file(...)` copies a project-relative file byte-for-byte. Its own exit status controls whether Git continues.
- Every hook name must be one of the hooks documented by Git. Duplicate declarations fail during Gradle configuration.

The DSL initializes hooks while Gradle configures the project; it does not attach an installer task to every build task. Git determines the hooks directory, so worktrees and `core.hooksPath` are respected. Managed SHA-256 state prevents Concord from overwriting unmanaged, symbolic, or locally modified hooks.

Removing a declaration is additive-only in version 0.1.0: it does not delete a previously initialized hook. Removing managed hooks and their adjacent `.plott-concord` state is an explicit repository-owner operation.

See [Git hooks behavior](docs/GIT_HOOKS.md) for supported names, execution context, arguments, ownership, and conflict recovery.

## Project structure

- `plugin/`: publishable Gradle plugin and JUnit test suite
- `sample/`: real Java/JUnit consumer using standard `test`
- `kr.co.plott.concord`: shared feature contracts and managed-file utilities
- `kr.co.plott.concord.git`: Git hook feature
- `kr.co.plott.concord.exception`: shared configuration and conflict errors

See [Architecture](docs/ARCHITECTURE.md) for responsibility and pattern rationale.

## Verification

The plugin uses one `test` source set with JUnit Jupiter. Tests cover command and file sources, all-name validation, custom `core.hooksPath`, configuration errors, managed-state conflicts, symlink protection, exact byte copying, and non-blocking command execution.

JaCoCo generates XML and HTML reports after tests. The current measured result is 196/200 covered lines (98.0%) and 65/74 covered branches (87.8%). No arbitrary coverage threshold is enforced.

## Releases and publication

Release tags must be final `vMAJOR.MINOR.PATCH` versions without leading zeroes. Every pushed tag is validated, and a non-prerelease GitHub Release automatically derives the plugin version and runs the single Plugin Portal publishing workflow. Manual publication dispatch is intentionally disabled.

See [Releasing and Plugin Portal approval](docs/RELEASING.md) for tag rules, Actions security, credentials, immutable versions, corporate namespace proof, and the current approval state.

## Security

Applying this plugin allows trusted Gradle configuration to create executable Git hooks. Review a repository's build logic and file-backed hooks before running Gradle. See [Security policy](SECURITY.md) for reporting and trust-boundary details.

## License

Copyright 2026 Handys Inc.

Plott Concord is licensed under the [Apache License 2.0](LICENSE). Attribution information is provided in [NOTICE](NOTICE).
