# Architecture

Plott Concord uses feature packages rather than horizontal DDD layers.

## Root package

`kr.co.plott.concord` contains only concepts shared by features:

- `ConcordPlugin`: composition root that constructs feature collaborators and registers the Gradle extension;
- `ConcordExtension`: top-level Gradle DSL;
- `Initializer<T>`: common initialization contract used by feature DSLs;
- `ManagedFileWriter`: shared atomic writing, SHA-256 ownership, conflict detection, and permission handling.

`kr.co.plott.concord.exception` contains shared configuration and ownership errors.

## Git feature package

`kr.co.plott.concord.git` owns the complete Git hook feature:

- `GitHooksExtension` and `GitHookSpec`: Builder-style Gradle DSL;
- `GitHookName`: validated value object containing Git's documented hook names;
- `GitHookSource`: sealed command/file strategy family;
- `GitHookContentResolver`: selects rendering or exact file reading;
- `GitHooksDirectoryResolver`: asks Git for the effective hooks path;
- `GitHookInitializer`: coordinates path, content, and managed writing.

The sealed source types are justified by two active behaviors: generated non-blocking commands and exact file content. No implementation-only strategy interface or speculative generic feature registry is present.

## Initialization model

Feature DSL calls initialize during Gradle configuration. This intentionally differs from an installer task attached to every build task. Initialization is idempotent and configuration-cache aware; no background daemon or persistent process is introduced.

## Testing strategy

Tests mirror production responsibilities rather than concentrating behavior in one integration test:

- value and Builder validation;
- source strategy dispatch;
- Git path resolution, including `core.hooksPath`;
- byte-preserving file reads;
- managed-file happy paths, migration, divergence, symbolic paths, and permissions;
- composition-root and real `sample/` consumption;
- tag resolver valid and invalid probes in repository validation.

JaCoCo reports coverage but does not convert coverage percentage into a quality proxy or release gate.
