# Build Backlog

Companion to [`findings.md`](./findings.md). The June 2026 fix pass closed the small, low-risk findings (theme wiring, button colors, dead spacing statements, the broken scripts, README typos, and similar). What is left here is the bigger work: features and infrastructure that need real design, tests, or a verifiable build before they land. Treat each entry as a unit of work to schedule, not a quick edit.

Severity carries over from `findings.md`: **High** means the project advertises something that does not run; **Medium** is worth doing before a release; **Low** is polish.

## High priority

### Implement the custom lint detectors
The headline feature of `chailinter` does nothing. Both detectors in `chailinter/src/main/java/com/droidconke/chailinter/detector/` register an empty `ISSUE`, so nothing flags raw Material colors or components. This is the single largest gap between what Chai claims and what it does. Build it test-first against `lint-tests`: define the real `Issue`s, write UAST scanning that catches `androidx.compose.material*` color and component usage inside Chai source, and add fixtures that prove each rule fires and stays quiet on correct code. Until this exists, `scripts/check-design-system-usage.sh` is the only thing actually enforcing the design system.

### Fill in the stub components
Five files are header-only but are presented as part of the system: `CCards.kt`, `CTabs.kt`, `Icons.kt`, `Images.kt`, and the renamed `CInputFields.kt`. Each needs an actual API designed against the Chai tokens, with previews and demo-screen coverage. These are design decisions, not fill-in-the-blank work, so scope them one component at a time.

## Medium priority

### Real component and theme tests
Every test in `chai` and `chaidemo` is a generated `2 + 2` scaffold. The README testing roadmap asks for component render tests, light/dark theme switching tests, accessibility semantics, and screenshot/visual regression. Pick a harness (Robolectric or instrumented Compose tests, plus a screenshot tool) and replace the scaffolds. The lint module needs its own tests too, tracked under the detector work above.

### Full dependency catalog pass
`gradle/libs.versions.toml` reflects an old project. Only a handful of versions were bumped recently. Do a deliberate sweep: run the Ben Manes `dependencyUpdates` task or the AGP Upgrade Assistant, move every entry to its current stable release, and lean on the Compose BOM instead of pinning `runtimeAndroid` / `uiTooling` / `material3Android` by hand. Either wire the already-added JUnit 6 entries into the modules or drop them, since modules still use JUnit 4. Build, then run lint, detekt, and tests, and read the Compose and AGP release notes for breaking changes before committing.

### Convention plugins (build logic)
The README and `docs/buildlogic.md` promise a `buildLogic/` convention-plugin setup, but configuration is copy-pasted across the three `build.gradle.kts` files (SDK levels, compose options, Java target). Extract the shared config into convention plugins so there is one source of truth. This also clears the leftover "see if I can add this to build Logic" notes in `chai/build.gradle.kts`.

### Raise the JVM target on the Android modules
`chai` and `chaidemo` compile to Java 8 bytecode while `chailinter` targets Java 17. Java 8 is unusually low for Kotlin 2.3 on compileSdk 36. Raise the two Compose modules to 11 or 17 for consistency, and verify the build after.

### Fill the empty docs
`docs/atoms.md` and `docs/components.md` are empty, and `docs/architecture.md` is a header only. The README links to all of them as if written. Each is its own commit per the repo doc rules.

## Low priority

### Edge-to-edge status bar
`Theme.kt` still sets `window.statusBarColor`, deprecated as of API 35. Move to the edge-to-edge APIs. This touches the host activity, so it is more than a one-line swap.

### Confirm or drop the multiplatform plugin
`chai/build.gradle.kts` applies both the Compose compiler plugin and `jetbrains.compose` (Compose Multiplatform) on an Android-only library. Confirm the multiplatform plugin is intentional or remove it.

### Rename `ChaiSteal` to `ChaiSteel`
`atoms/Color.kt` defines `ChaiSteal` (a typo for "Steel") and the name propagates through `colors/ChaiColors.kt`. This is a breaking change for any external consumer, so it was left alone. Do it as a coordinated rename if and when a breaking release is acceptable.
