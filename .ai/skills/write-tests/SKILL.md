---
name: write-tests
description: |
  Write or extend tests for the Chai repo. Triggered by "write a test",
  "add tests", "test this", or as the follow-up when a component/util lands
  without coverage. Local unit tests use JUnit 6 (Jupiter); Compose/UI behaviour
  is tested as instrumented tests. Picks the right source set and idioms; does
  not invent mocking/assertion libraries the catalog does not declare.
---

# write-tests

The repo's tests are still mostly generated scaffolds (see
`docs/tech/findings.md` → "Only scaffold tests exist"). This skill writes real
ones in the conventions already wired into the build.

## What runs where

| Kind | Source set | Framework | Run with |
|------|-----------|-----------|----------|
| Pure logic, no Android/Compose runtime | `src/test/java` | JUnit 6 (Jupiter), `useJUnitPlatform()` | `./gradlew :<module>:testDebugUnitTest` |
| Composable rendering / interaction | `src/androidTest/java` | `androidx.compose.ui:ui-test-junit4` + `androidx.test.ext:junit` | `./gradlew :<module>:connectedDebugAndroidTest` (needs a device/emulator) |

Catalog references (`gradle/libs.versions.toml`): `junit-bom`,
`junit-jupiter`, `junit-platform-launcher` for unit tests;
`androidx-ui-test-junit4`, `androidx-ui-test-manifest`, `androidx-junit`,
`androidx-espresso-core` for instrumented.

## Rules

- **Use Jupiter, not JUnit 4, for unit tests.** Import `org.junit.jupiter.api.Test`
  and `org.junit.jupiter.api.Assertions.*` (or `assertEquals`/`assertTrue` from
  Jupiter). Do not import `org.junit.Test` or `org.junit.Assert` in `src/test`.
- **No new test libraries.** The catalog declares no MockK, Truth, Turbine, or
  coroutines-test. Do not add them as part of writing a test — if a test truly
  needs one, raise it as a separate dependency change (see the `dependency-bump`
  skill) rather than slipping it in here.
- **Compose tests** go in `src/androidTest`, use `createComposeRule()` /
  `createAndroidComposeRule<…>()`, and wrap content in `ChaiTheme { }` so tokens
  resolve the same way they do in the app.
- **Design system still applies in test content.** When a test renders sample
  UI, use Chai components/tokens, not raw Material — the same guard rail
  (`scripts/check-design-system-usage.sh`) covers test sources.
- Replace generated `ExampleUnitTest` / `ExampleInstrumentedTest` stubs when you
  add the first real test for a module; do not leave the `2 + 2 == 4` scaffold
  next to real coverage.

## Procedure

1. Find what is untested. Pick the smallest meaningful unit (a util, a mapper, a
   component's documented behaviour).
2. Choose the source set from the table above. Pure Kotlin → `src/test`.
   Anything that composes → `src/androidTest`.
3. Name the file `<TypeName>Test.kt` in the matching package under that source
   set. One behaviour per `@Test`; name tests for the behaviour, not the method.
4. Run the matching gradle task (use the `check-build` / `checks` skills if the
   module fails to compile first).
5. Report: file added, what it covers, and the run result.

## Report

```
write-tests  ✓   <module>: added <N> test(s) in <file>   (testDebugUnitTest passed)
```

If instrumented tests were written but no device was available to run them, say
so explicitly — do not claim they passed.
