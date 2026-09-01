# Security policy

## Reporting

Do not disclose a suspected command-injection, path-traversal, hook-ownership, or credential issue in a public issue before maintainers have assessed it. Use **Report a vulnerability** in the repository's GitHub Security tab; private vulnerability reporting is enabled.

## Trust boundaries

- Gradle build logic is trusted input. Applying the plugin can create executable Git hooks.
- Command-backed hook strings are embedded as shell commands and must come from reviewed build configuration.
- File-backed hooks must remain inside the project, must be regular files, and cannot be symbolic links.
- Git selects the effective hooks directory, including `core.hooksPath`; repository configuration can therefore direct writes outside the working tree.
- Concord refuses unmanaged, symbolic, and locally modified managed destinations.
- Plugin Portal credentials exist only as GitHub environment secrets and must never be committed.

## Supported versions

Security fixes are applied to the latest released version. Version 0.1.1 is awaiting initial Plugin Portal approval.
