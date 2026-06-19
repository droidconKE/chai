---
name: check-build
description: |
  Verify the Chai project assembles. Triggered by "check-build",
  "/check-build", "does it build", or by checks as the assemble gate.
  Runs `./gradlew assembleDebug` only — no mutation, no formatting.
---

# check-build

One gradle task, then report. No edits.

## Pre-flight

- `pwd` is the repo root.
- `./gradlew --version` runs.

## Step 1 — Run

```bash
./gradlew assembleDebug
```

Capture stdout, stderr, and the exit code. For a single module, scope it (`./gradlew :chai:assembleDebug`).

## Step 2 — Report

Success:

```
check-build  ✓   assembleDebug passed   (<duration>)
```

Failure:

```
check-build  ✗   assembleDebug failed

<first compiler/Gradle error: file:line: message>

<N> errors. Fix the first; later ones often cascade.
```

Pull the first real error (a Kotlin `e: file.kt:line:col` line or a Gradle `* What went wrong` block), not the final "BUILD FAILED" summary.

## Note

If the failure is a Gradle configuration-cache or "false negative" sync error, suggest `./gradlew clean assembleDebug` once (the README documents these as common). Do not loop retries.
