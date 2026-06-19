---
name: check-lint
description: |
  Run Android Lint on Chai and report. Triggered by "check-lint",
  "/check-lint", "run lint", or by checks as the lint gate. Runs
  `./gradlew lintDebug` only — reports the first violation, never edits.
---

# check-lint

One gradle task, then report. No mutation.

## Pre-flight

- `pwd` is the repo root.

## Step 1 — Run

```bash
./gradlew lintDebug
```

Capture stdout, stderr, exit code. Lint also writes an HTML/XML report under each module's `build/reports/lint-results-debug.*` — point the user there for the full list.

## Step 2 — Report

Success:

```
check-lint  ✓   lintDebug passed   (<duration>)
```

Failure:

```
check-lint  ✗   lintDebug failed

First issue:
  <file>:<line>  <IssueId>  <message>

<N> issues. Full report: <module>/build/reports/lint-results-debug.html
```

## Notes

- This repo has **no** `ktlint` or `detekt` task — do not invent one. Style/formatting is meant to be Spotless (currently broken, see `.ai/memory/reference_build_and_checks.md`) plus Android Lint plus the custom `chailinter`.
- `chailinter`'s detectors are non-functional stubs today, so design-system rules are NOT enforced by lint yet. Use the `audit-design-system-usage` skill for that until the detectors are implemented.
- Do not suppress issues or edit `.editorconfig` to make lint pass.
