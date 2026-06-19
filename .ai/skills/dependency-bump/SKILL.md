---
name: dependency-bump
description: |
  Update gradle/libs.versions.toml toward latest stable and verify safely.
  Triggered by "dependency-bump", "/dependency-bump", "update
  dependencies", "bump versions". Follows the dependency checklist in
  docs/tech/findings.md.
---

# dependency-bump

You move the version catalog toward latest stable and prove the build still works. Catalog is the single source of truth: `gradle/libs.versions.toml`. Edit versions there, never pin in module `build.gradle.kts`.

## Step 1 — See what's behind

```bash
./gradlew dependencyUpdates   # if the Ben Manes plugin is present; else inspect the catalog by hand
```

Cover the whole catalog, not just the toolchain: `agp`, `kotlin`, `composeBom`, `lint`/`lintTests`, `coreKtx`, `appcompat`, `material`, `material3`/`material3Android`, lifecycle, activity-compose, the Compose `runtime`/`ui-tooling` pins, and the test stack (`androidx.test.ext:junit`, `espressoCore`).

## Step 2 — Bump deliberately

- Prefer driving Compose artifact versions through the **Compose BOM** rather than individual `runtimeAndroid` / `uiTooling` / `material3Android` pins. Several pins exist only because the BOM isn't relied on consistently.
- Keep `kotlin` and the `compose-compiler` plugin in lockstep (same `kotlin` ref).
- Drop the ignored `composeOptions { kotlinCompilerExtensionVersion = … }` in module build files — it does nothing under Kotlin 2.x.
- Don't leave catalog entries that no module uses (e.g. the unused JUnit 6 entries). Either wire them in or remove them.

## Step 3 — Verify

```bash
./gradlew clean lintDebug testDebugUnitTest assembleDebug --configuration-cache
```

Read the **release notes** for any major bump (AGP, Kotlin, Compose) and check known deprecations in `findings.md` (e.g. `statusBarColor` on API 35, manifest `package` attribute).

## Step 4 — Commit

Treat the bump as its own change, on a `fix/…` or `chore`-style `feature/deps-…` branch off `develop`. Atomic: the catalog change in one commit (`Updated dependencies to latest versions`); any code fixes for breaking changes in follow-up commits. Per `commit-convention`. Do not bundle unrelated work.

## Guardrail

A version bump that doesn't build is worse than none. If verification fails and the fix isn't small, stop and report which dependency broke rather than chasing a cascade.
