# Changelog

## 0.1.1 - 2026-09-01

- Licensed the project under Apache License 2.0 with copyright held by Handys Inc.
- Restricted declarations to Git's documented hook names and rejected duplicates.
- Hardened managed state against symbolic links, invalid paths, and local divergence.
- Pinned GitHub Actions, scoped publishing credentials, and removed manual publication.
- Expanded public hook, architecture, security, release, and approval documentation.

## 0.1.0 - 2026-09-01

- Added Gradle DSL initialization for every documented Git hook name.
- Added non-blocking command-backed hooks and exact file-backed hooks.
- Added effective hooks-path discovery with worktree and `core.hooksPath` support.
- Added SHA-256 managed ownership, atomic replacement, conflict protection, and executable permissions.
- Added a real Java/JUnit sample, responsibility-focused tests, and JaCoCo reporting.
- Added SemVer tag validation and Gradle Plugin Portal release automation.
