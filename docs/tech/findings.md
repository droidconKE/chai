# Code Analysis Findings

A review of the Chai design system project across its three modules (`chai`, `chaidemo`, `chailinter`), plus build, CI, tooling, and documentation. Findings reference source by `path:line` rather than embedding code.

## Summary

The bones are good. There is a semantic color palette, typography atoms, a theme wrapper, and a few button components with previews. The problem is the gap between what the project advertises and what actually runs. The custom lint meant to enforce the design system does nothing. Several promised components are empty stubs, the core docs are empty, and there are no real tests. These are mostly correctness and completeness gaps, not deep design mistakes, which is the good news: they are fixable without rethinking the architecture.

Severity legend: **High** (broken or misleading), **Medium** (should fix before release), **Low** (polish).

## Resolution status

A fix pass in June 2026 closed the low-risk findings. Resolved since this audit was written:

- `ChaiColors` is now wired into `MaterialTheme`, and the buttons read the palette directly. The "palette is decorative" and "`CPrimaryButton` has no container color" findings no longer hold.
- The dead `Space5` / `Space30` statements are real `Spacer`s now, both in `CButtons.kt` and the demo screen.
- The buttons demo screen renders the actual components instead of a lone text label.
- Typography uses `staticCompositionLocalOf`, matching the colors token.
- The ignored `kotlinCompilerExtensionVersion` is gone from both Android modules.
- The duplicate copyright header in the lint build script is removed.
- `CInputFiels.kt` is renamed to `CInputFields.kt` (the file is still a stub; see backlog).
- `tools/setup.sh` has a working curl line, the broken `gradle/init.gradle.kts` spotless references are removed from the Makefile and pre-push, and `todo.yml` now watches `develop`.
- README typos and the bogus "chaidemop" link are fixed.

A second pass on this branch closed more of the build and tooling findings:

- The two Android modules now compile to Java 21 (`jvmToolchain(21)`), matching `chailinter`; the Java 8 target and cross-module JVM mismatch are gone.
- `chai/build.gradle.kts` dropped the `jetbrains.compose` multiplatform plugin; it applies only `android.library` and `compose.compiler`.
- `Theme.kt` draws edge-to-edge via `WindowCompat` instead of the deprecated `window.statusBarColor`.
- The JUnit 6 (Jupiter) catalog entries are wired into both Android modules with `useJUnitPlatform()`.
- The `make spotless` / pre-push `init.gradle.kts` references, the corrupted `tools/setup.sh` curl line, and the `todo.yml` branch are all corrected; the `kotlinCompilerExtensionVersion` and manifest `package=` leftovers are gone.

Still open, with detail in [`backlog.md`](./backlog.md): the lint detectors, the remaining stub components, real tests, the full dependency pass, convention plugins, the empty docs, and the `ChaiSteal` rename.

A few items in the original audit no longer apply to the current tree: the README links to `docs/chaiArchitecture.md`, `docs/atoms.md`, and `docs/components.md` all resolve to real files.

## Incomplete and stub implementations

**Header-only component files. (High)**
The following contain only a license header and a package declaration (15 lines each), no implementation:
- `chai/src/main/java/com/droidconke/chai/components/CCards.kt`
- `chai/src/main/java/com/droidconke/chai/components/CTabs.kt`
- `chai/src/main/java/com/droidconke/chai/icons/Icons.kt`
- `chai/src/main/java/com/droidconke/chai/images/Images.kt`
- `chai/src/main/java/com/droidconke/chai/components/CInputFiels.kt` (also misnamed; see below)

The README and docs present these as part of the system, but the APIs do not exist yet.

**Misspelled filename: `CInputFiels.kt`. (Medium)**
`chai/src/main/java/com/droidconke/chai/components/CInputFiels.kt` should be `CInputFields.kt`. The file is also an empty stub.

**Misspelled color atom: `ChaiSteal`. (Low)**
`atoms/Color.kt:41` defines `ChaiSteal` (intended "Steel"). The typo propagates through every reference in `colors/ChaiColors.kt`. Renaming is a breaking change for any external consumer, so weigh it against current adoption.

## Build configuration

**Leftover "build logic" TODO comments. (Low)**
`chai/build.gradle.kts:41` and `:48` carry `//See if i can add this to build Logic`. The root `build.gradle.kts` has a similar note. These point at the missing convention-plugin work described under Documentation below.

## Custom lint module (`chailinter`)

**The detectors do nothing. (High)**
Both detectors expose an `ISSUE` set to `EMPTY_ARRAY`:
- `chailinter/src/main/java/com/droidconke/chailinter/detector/IncorrectColourUsageDetector.kt:25` (the class has no scanner overrides at all)
- `chailinter/src/main/java/com/droidconke/chailinter/detector/ChaiIncorrectUsageDetector.kt` (its `METHOD_NAMES` map is empty, with a "To be implemented" comment)

They are registered in `ChaiLinterIssueRegistry.kt`, but registration is meaningless with empty issues. The headline feature, lint that forces use of the design system over raw Material colors, is not implemented. This is the single biggest gap between the project's stated purpose and its behavior.

**No lint tests. (Medium)**
`chailinter/build.gradle.kts` pulls in `lint-tests` and JUnit, but there is no test source directory. Custom lint rules are normally developed test-first against `lint-tests`; none exist.

**Duplicate copyright header in the build script. (Low)**
`chailinter/build.gradle.kts` carries two license headers (lines 1-15 and 18-31).

> Note: `chailinter/bin/` mirrors `src/main` on disk but is correctly listed in `.gitignore` (`bin/`) and is not tracked. No action needed; it is a local IDE build artifact.

## Testing

**Only scaffold tests exist. (Medium)**
Every test file is a generated stub:
- `chai/src/test/java/.../ExampleUnitTest.kt` and `.../ExampleInstrumentedTest.kt`
- `chaidemo/src/test/java/.../ExampleUnitTest.kt` and `.../ExampleInstrumentedTest.kt`

They assert `2 + 2 == 4` and the package name. There are no component render tests, no theme/dark-mode tests, and no screenshot tests, despite the README's testing roadmap calling for exactly these. The custom lint has no tests either (above).

## CI, tooling, and scripts

**The main CI workflow is solid. (Informational)**
`.github/workflows/main.yml` is well structured: parallel jobs, Gradle configuration cache and build cache, AVD caching, and conditional instrumented tests gated on branch or a `[test-instrumented]` commit-message flag. `gradle.properties` is tuned for CI (parallel, caching, 4 workers, 4 GB heap).

## Documentation

**Core docs are empty or header-only. (Medium)**
The README links to these as if they were written, but each is empty or a single header line:
- `docs/atoms.md` (0 bytes)
- `docs/components.md` (0 bytes)
- `docs/architecture.md` (header only)
- `docs/chaiArchitecture.md` (header only)
- `docs/chaiLinter.md` (header only)
- `docs/buildlogic.md` (header only)

**Documented build-logic does not exist. (Medium)**
The README and `docs/buildlogic.md` promise a `buildLogic/` convention-plugin setup. There is no such module; configuration is repeated inline across the three `build.gradle.kts` files (compileSdk, minSdk, compose options, Java target), which is exactly what convention plugins would remove.

## Dependency and version state

A full sweep of `gradle/libs.versions.toml` against the live Maven repositories confirms every entry is at its current latest stable release (AGP 9.2.1, Kotlin 2.4.0, Compose BOM 2026.06.00, lint 32.2.1, JUnit BOM 6.1.0, and the AndroidX stack). The JUnit 6 (Jupiter) entries are wired into both Android modules with `useJUnitPlatform()`, and the old JUnit 4 entry is gone.

One non-version observation remains: the catalog still pins `runtime-android`, `material3-android`, and `ui-tooling-preview-android` individually rather than letting the Compose BOM drive them, and `kotlin-stdlib` references the merged-away `kotlin-stdlib-jdk8` artifact (and is not referenced by any module). Worth tidying, but neither is a version gap.

## Prioritized recommendations

The theme wiring, button colors, JVM target, JUnit wiring, dependency versions, and tooling-script repairs are done (see Resolution status). What remains, in priority order, is tracked in detail in [`backlog.md`](./backlog.md):

1. **Implement the lint detectors or remove the module's claims.** As shipped, the design-system enforcement is non-functional. Add `lint-tests`-based tests as you go. (chailinter)
2. **Fill in or implement the stubs:** the empty component files, the empty docs, and the demo screens for the components that don't yet exist.
3. **Add real tests** for component rendering and theme switching, replacing the scaffold tests.
4. **Extract shared build config into convention plugins** to match the documentation and clear the leftover "build Logic" notes.
