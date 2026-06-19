# Code Analysis Findings

A review of the Chai design system project across its three modules (`chai`, `chaidemo`, `chailinter`), plus build, CI, tooling, and documentation. Findings reference source by `path:line` rather than embedding code.

## Summary

The bones are good. There is a semantic color palette, typography atoms, a theme wrapper, and a few button components with previews. The problem is the gap between what the project advertises and what actually runs. The custom lint meant to enforce the design system does nothing. Several promised components are empty stubs. The color palette is wired into the theme but the components never read from it. A couple of tooling scripts point at files that were never created. These are mostly correctness and completeness gaps, not deep design mistakes, which is the good news: they are fixable without rethinking the architecture.

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

Still open, with detail in [`backlog.md`](./backlog.md): the lint detectors, the remaining stub components, real tests, the full dependency pass, convention plugins, the Java 8 target, the empty docs, edge-to-edge status bar, the multiplatform plugin, and the `ChaiSteal` rename.

A few items in the original audit no longer apply to the current tree: the manifests carry no `package=` attribute, and the README links to `docs/chaiArchitecture.md`, `docs/atoms.md`, and `docs/components.md` all resolve to real files.

## Architecture and design-system correctness

**The custom `ChaiColors` palette is defined but barely used. (High)**
`ChaiTheme` (`chai/src/main/java/com/droidconke/chai/Theme.kt:81`) provides `LocalChaiColorsPalette` through a `CompositionLocalProvider`, but calls `MaterialTheme(content = content)` with no `colorScheme` argument. So `MaterialTheme.colorScheme` stays the stock Material 3 default. The components then read from the Material scheme instead of the Chai palette: `CPrimaryButton` uses `MaterialTheme.colorScheme.primary` (`chai/src/main/java/com/droidconke/chai/components/CButtons.kt:89-90`). The result is that the 38-field semantic palette in `colors/ChaiColors.kt:43-82` is mostly dead: it is plumbed into the theme but the shipped components do not consume it. This undercuts the project's central premise (a design system that drives color usage).

**`CPrimaryButton` never sets a container color. (High)**
`CButtons.kt:88-91` builds `ButtonDefaults.buttonColors` with only `contentColor` and `disabledContentColor`. The button's background falls back to the Material default container color, so the "primary" button is not visibly branded. It also sets `contentColor` to `colorScheme.primary`, which is the brand color, making text the brand color on a default container rather than the inverse you would expect for a primary button.

**Spacing values used as no-op statements. (High)**
`Space5`, `Space15`, `Space30` are `Dp` values (`utils/Spacing.kt:32-34`), not composables. They are written as bare statements where a `Spacer` is intended:
- `CButtons.kt:113` (`Space5` between the icon and the label in `COutlinedPrimaryButton`)
- `chaidemo/src/main/java/com/droidconke/chaidemo/screens/ChaiButtonsDemoScreen.kt:26` (`Space30`)

These render nothing. The intended API is the `Spacer5()` / `Spacer15()` / `Spacer30()` composables that already exist in `utils/Spacing.kt:46-58`, or `Spacer(Modifier.width(...))` for horizontal gaps.

**Inconsistent `CompositionLocal` factory choice. (Medium)**
Colors use `staticCompositionLocalOf` (`colors/ChaiColors.kt:84`); typography uses `compositionLocalOf` (`typography/ChaiTypography.kt`). For design tokens that change rarely, `staticCompositionLocalOf` is the usual choice. The mismatch gives the two token families different recomposition behavior for no stated reason.

**`statusBarColor` is deprecated. (Low)**
`Theme.kt:71` sets `activity.window.statusBarColor`, deprecated as of API 35. With `compileSdk = 36` this will surface as a lint/deprecation warning; the edge-to-edge APIs are the current path.

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

**Demo app does not demonstrate the components. (Medium)**
`ChaiButtonsDemoScreen.kt` is named for buttons but renders only a text label and the dead `Space30` statement; no `CButton`, `CPrimaryButton`, or `COutlinedPrimaryButton` appears. The demo also covers none of `CCards`, `CTabs`, input fields, or tabs. A sample app is the main way consumers learn a design system, so this is a real gap.

## Build configuration

**`targetSdk` mixing and JVM target inconsistency across modules. (Medium)**
- `chai/build.gradle.kts` sets `compileSdk = 36`, `minSdk = 28`, and Java/JVM target 1.8.
- `chaidemo/build.gradle.kts` sets the same SDKs with `targetSdk = 36`, Java/JVM target 1.8.
- `chailinter/build.gradle.kts` uses Java 17 / `JvmTarget.JVM_17`.

The two Android modules compile to Java 8 bytecode while the lint module targets Java 17. That is workable (lint runs on the host JVM), but the Java 8 target for Compose modules on Kotlin 2.3 / compileSdk 36 is unusually low and worth raising to 11 or 17 for consistency.

**Stale `kotlinCompilerExtensionVersion`. (Medium)**
Both `chai/build.gradle.kts:67` and `chaidemo/build.gradle.kts` set `composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }`. With Kotlin 2.x the Compose compiler is applied via the `org.jetbrains.kotlin.plugin.compose` Gradle plugin (already present), and this `composeOptions` value is ignored. It is dead config that implies an older compiler than is actually in use.

**Manifest `package` attribute alongside `namespace`. (Low)**
Several module manifests still declare `package="com.droidconke.chai"` (for example the `chai` manifests) while the build files also set `namespace`. The manifest `package` attribute is deprecated in AGP 8; `namespace` in Gradle is the source of truth.

**Leftover "build logic" TODO comments. (Low)**
`chai/build.gradle.kts:43` and `:50` carry `//See if i can add this to build Logic`. The root `build.gradle.kts` has a similar note. These point at the missing convention-plugin work described under Documentation below.

**`jetbrains.compose` plugin on an Android-only library. (Low)**
`chai/build.gradle.kts` applies both `compose.compiler` and `jetbrains.compose` (Compose Multiplatform). For an Android-only design system the multiplatform plugin is likely redundant; confirm it is intentional or drop it.

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

**`make spotless` and the pre-push `check` reference a missing file. (High)**
`Makefile:30` and `tools/pre-push:91` both run `./gradlew --init-script gradle/init.gradle.kts ...`, but `gradle/init.gradle.kts` does not exist (only `libs.versions.toml` and `wrapper/` are in `gradle/`). Both commands fail. Spotless is configured under `spotless/` but is not applied in any `*.gradle.kts`, so there is no working `spotless` task regardless.

**`tools/setup.sh` has a corrupted curl line. (High)**
`tools/setup.sh:25` reads `curl -sSLo "${GIT_DIR}/hooks/commit-msg" \653 jym5` with garbage where the URL/continuation belongs. Running the setup script fails to install the commit-msg hook.

**TODO-to-issue workflow watches the wrong branch. (Medium)**
`.github/workflows/todo.yml:7` triggers on `dev`, but the repository's integration branch is `develop` (see recent history and PR merges). The automation never fires on the branch that is actually used.

**Otherwise the main CI workflow is solid. (Informational)**
`.github/workflows/main.yml` is well structured: parallel jobs, Gradle configuration cache and build cache, AVD caching, and conditional instrumented tests gated on branch or a `[test-instrumented]` commit-message flag. `gradle.properties` is tuned for CI (parallel, caching, 4 workers, 4 GB heap).

## Documentation

**Several core docs are empty. (Medium)**
- `docs/chaiLinter.md` (0 bytes)
- `docs/chaiProjectArchitecture.md` (0 bytes)
- `docs/atoms/atoms.md` (0 bytes)
- `docs/components/components.md` (0 bytes)
- `docs/architecture.md` (header only)

The README links to these as if they were written.

**Broken and duplicated README links. (Medium)**
In `README.md`:
- Links to `docs/chaiArchitecture.md`; the file is `docs/chaiProjectArchitecture.md`.
- Links to `docs/buildlogic.md`; no such file exists, and the convention-plugin infrastructure it describes is not present either (no `build-logic/` or `buildLogic/` module).
- Links to `docs/atoms.md` and `docs/components.md`; the files live at `docs/atoms/atoms.md` and `docs/components/components.md`.
- The "chaidemop" entry (line 38) is a typo for chaidemo, has the wrong description, and duplicates the components link.

**Documented build-logic does not exist. (Medium)**
The README and task list promise a `buildLogic/` convention-plugin setup. There is no such module; configuration is repeated inline across the three `build.gradle.kts` files (compileSdk, minSdk, compose options, Java target), which is exactly what convention plugins would remove.

**README typos. (Low)**
`README.md` contains "wiyth", "convengion polugins", "ist probably", and "ana ctions" (lines 34, 71, 74), among others.

## Dependency and version state

The working tree has uncommitted version bumps in `gradle/libs.versions.toml` on branch `project-fix-reset`:
- Kotlin 2.2.20 → 2.3.0
- AGP 8.13.0 → 8.13.2
- Compose BOM 2025.10.01 → 2026.01.00
- lifecycle, activity-compose, runtime, ui-tooling bumped
- JUnit 6 (Jupiter) entries added

The JUnit 6 entries are added to the catalog but not wired into any module (modules still use JUnit 4 via `libs.junit`). Either finish the migration or drop the unused entries to avoid confusion. Decide whether this version bump is intentional before committing.

**Bring the whole catalog up to date. (Medium)**
The bumps above are a start, but the project should do a deliberate pass over the entire `gradle/libs.versions.toml` and move every dependency to its current stable release, not just the handful already touched in the working tree. This is an old project and the versions reflect that. Concretely:
- Run `./gradlew dependencyUpdates` (Ben Manes versions plugin) or use Android Studio's "AGP Upgrade Assistant" plus catalog inspection to list what is behind.
- Cover the rest of the catalog that was not bumped: `androidx.core:core-ktx`, `appcompat`, `material` (the Google Material View library), `material3` / `material3-android`, the Compose `runtime`/`ui-tooling` entries, the lint tooling (`lint`, `lint-tests`, currently `32.0.0`), and the test stack (`androidx.test.ext:junit`, `espresso-core`).
- Prefer driving Compose artifact versions through the Compose BOM rather than pinning `runtimeAndroid` / `uiTooling` / `material3Android` individually; several of those pins exist only because the BOM is not being relied on consistently.
- Treat the update as its own change: bump, build, run `lint` / `detekt` / tests, and check the Compose and AGP release notes for breaking changes (especially around the deprecated `statusBarColor` and manifest `package` items noted earlier). Keep it on this branch and out of unrelated commits.

## Prioritized recommendations

Items 1, 2, and 4 are done, and 5 and 7 are partly done (see Resolution status above). The rest are tracked in [`backlog.md`](./backlog.md).

1. **Wire `ChaiColors` into the theme.** Pass a `colorScheme` derived from the Chai palette into `MaterialTheme`, or have components read `ChaiTheme.colors` / `LocalChaiColorsPalette` directly. Today the palette is decorative. (Theme.kt, CButtons.kt)
2. **Fix `CPrimaryButton` colors** so the primary button has a branded container, and replace the bare `Space*` statements with `Spacer*()` calls. (CButtons.kt, ChaiButtonsDemoScreen.kt)
3. **Implement the lint detectors or remove the module's claims.** As shipped, the design-system enforcement is non-functional. Add `lint-tests`-based tests as you go. (chailinter)
4. **Repair the tooling scripts:** create or remove `gradle/init.gradle.kts`, actually apply Spotless, fix the `setup.sh` curl line, and point `todo.yml` at `develop`.
5. **Fill in or implement the stubs:** the empty component files, the empty docs, and the demo screens. Fix the README links and typos while you are there.
6. **Add real tests** for component rendering and theme switching, replacing the scaffold tests.
7. **Clean up build config:** drop the ignored `kotlinCompilerExtensionVersion`, reconsider the Java 8 target, and finish or revert the JUnit 6 / version-catalog changes. Extract shared config into convention plugins to match the documentation.
8. **Update dependencies to latest.** Do a full pass over `gradle/libs.versions.toml`, not just the few versions already bumped. Lean on the Compose BOM, then build and run lint, detekt, and tests to catch breakage. (See "Dependency and version state".)
