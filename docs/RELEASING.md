# Releasing and Plugin Portal approval

## Version source

Git tags are the only release-version source. The accepted format is `vMAJOR.MINOR.PATCH`, where each numeric component is zero or a non-zero digit followed by digits. Pre-release suffixes and leading zeroes are rejected.

`.github/scripts/resolve-version.sh` is the single parser used by tag validation and publication. It removes the leading `v` after validation.

## Actions flow

1. Every push and pull request to `main` runs plugin checks and the real sample test.
2. Every pushed tag runs `Validate release tag`.
3. Publishing requires a published, non-prerelease GitHub Release.
4. The publish job validates the tag again, checks the plugin, and publishes once.

The publish workflow has no manual dispatch, uses a non-cancelling concurrency group, has read-only repository permission, and scopes Plugin Portal credentials to the `gradle-plugin-portal` GitHub environment. Actions are pinned to reviewed commit SHAs and Dependabot tracks updates.

Plugin Portal versions are immutable. A version must never be republished, and only final versions are accepted.

## Initial approval

Version `0.1.0` was successfully submitted on 2026-09-01 and is waiting for the initial manual review. No additional version should be submitted merely to poll or refresh that review.

The project addresses the official approval criteria as follows:

- Functional plugin: command- and file-backed Git hooks are initialized in effective Git hook directories.
- Broad utility: the feature is repository-agnostic and does not depend on private Handys services.
- English documentation: README and detailed public documents are in this repository.
- Metadata: description, tags, project URL, and VCS URL point to the public source.
- Coordinates: plugin ID `kr.co.plott.concord` and group `kr.co.plott` share the same namespace.
- Final release: `0.1.0` is not a snapshot.
- Original public repository: `handyskr/gradle-plott-concord` is not a fork.

The namespace is corporate. Gradle may request a DNS TXT record proving control of `plott.co.kr`, which corresponds to the reversed `kr.co.plott` namespace. The exact record must come from the Gradle reviewer and must not be guessed in advance.

The repository is licensed under Apache License 2.0 with copyright held by Handys Inc. The standard license text, NOTICE attribution, README, and publication POM metadata must remain consistent.

Official criteria: [Plugin Portal approval](https://plugins.gradle.org/docs/publish-plugin#approval).
