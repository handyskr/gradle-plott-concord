# Git hooks feature

The Git hooks feature initializes executable files in the hooks directory reported by `git rev-parse --git-path hooks`. This respects standard repositories, linked worktrees, and `core.hooksPath`.

## DSL

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

Each hook accepts exactly one source.

### Command source

`command(...)` embeds the declared command in a generated POSIX shell wrapper. The wrapper prints a diagnostic when the command fails and always exits zero. It is suitable for advisory checks that must not block Git.

The command runs with the arguments and standard input Git gives the generated hook. Commands that need hook arguments must reference them explicitly. Git normally runs client-side hooks from the working-tree root, but push-related and server-side hooks can run from the Git directory. Prefer a file source when hook-specific arguments, input, or working-directory behavior matters.

### File source

`file(...)` resolves a path relative to the root Gradle project. Absolute paths, escaping paths, missing files, and symbolic source files are rejected. The source bytes are copied exactly, and the destination is made executable. The file controls its own exit status and can therefore enforce or reject the Git operation.

## Supported hook names

Plott Concord accepts the current hooks documented by Git:

- Commit workflow: `pre-commit`, `pre-merge-commit`, `prepare-commit-msg`, `commit-msg`, `post-commit`
- Patch workflow: `applypatch-msg`, `pre-applypatch`, `post-applypatch`
- Working-tree workflow: `pre-rebase`, `post-checkout`, `post-merge`, `post-rewrite`, `post-index-change`
- Push workflow: `pre-push`, `pre-receive`, `update`, `proc-receive`, `post-receive`, `post-update`, `reference-transaction`, `push-to-checkout`
- Maintenance and integrations: `pre-auto-gc`, `sendemail-validate`, `fsmonitor-watchman`, `p4-changelist`, `p4-prepare-changelist`, `p4-post-changelist`, `p4-pre-submit`

Server-side hook files can be initialized in a local repository, but this plugin cannot install hooks on GitHub's remote servers.

## Ownership and updates

For each destination, Concord stores a SHA-256 value under `.plott-concord` next to the hooks. An update is allowed only when the current destination still matches its recorded hash. Concord refuses to replace:

- an unmanaged file with different content;
- a managed file changed outside Concord;
- a symbolic destination or symbolic state directory;
- a non-regular destination or invalid state path.

Initialization uses atomic replacement when the filesystem supports it and a replace fallback otherwise. Command and file sources are idempotent when their content has not changed.

Removing a DSL declaration does not delete the hook in version 0.1.0. Repository owners must deliberately remove both the hook and its matching state file when retiring a managed hook.

## Configuration-cache behavior

Git path discovery uses Gradle's provider-based process API. The first configuration that changes managed hook content can invalidate a prior cache fingerprint once; subsequent unchanged builds reuse the configuration cache without running another installer task.
