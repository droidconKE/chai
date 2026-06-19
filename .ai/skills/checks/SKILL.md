---
name: checks
description: |
  Umbrella pre-PR gate for Chai. Triggered by "checks", "/checks", "run
  the checks", or by any agent that needs a green light before committing
  or opening a PR. Runs the design-system audit, Android Lint, unit tests,
  and a debug assemble, then reports a single pass/fail.
---

# checks

You run the repo's pre-PR gate in order and report one verdict. Verify-only: you do not auto-fix or commit.

## Pre-flight

- `pwd` is the repo root (where `gradlew` lives).
- `./gradlew --version` runs. If the JDK is wrong for AGP 8.13, stop and tell the user.

## Steps (stop reporting which one failed, but run all that can run)

1. **Design system** — run `scripts/check-design-system-usage.sh`. Fails on raw Material / hardcoded colors in component code.
2. **Lint** — `./gradlew lintDebug` (or invoke the `check-lint` skill).
3. **Unit tests** — `./gradlew testDebugUnitTest`.
4. **Assemble** — `./gradlew assembleDebug` (or the `check-build` skill).

Tip: the CI-equivalent single command is
`./gradlew clean lintDebug assembleDebug testDebugUnitTest --configuration-cache`.

## Report

```
checks  ✓   design-system · lint · test · assemble   (<total duration>)
```

On any failure:

```
checks  ✗   failed at: <step>

<first error line(s)>

Next: <the specific fix or skill to run, e.g. audit-design-system-usage / check-lint>
```

## Not this skill's job

- Does not format (spotless is currently broken — see `.ai/memory/reference_build_and_checks.md`).
- Does not run instrumented tests (`connectedCheck`) — those need a device and are gated by a `[test-instrumented]` commit tag in CI.
- Does not commit or push.
