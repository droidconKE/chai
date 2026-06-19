---
name: audit-design-system-usage
description: |
  Find design-system violations in Chai source — raw Material components,
  hardcoded colors, bare .dp magic numbers, @Preview instead of
  @ChaiPreview. Triggered by "audit-design-system-usage", "/audit", or by
  checks/code-reviewer. This is the check the broken chailinter is meant
  to do. Reports; does not edit.
---

# audit-design-system-usage

You find places where source bypasses the design system. The custom `chailinter` is supposed to do this but its detectors are non-functional stubs, so this skill is the stand-in until they're implemented.

## Run the script first

```bash
scripts/check-design-system-usage.sh
```

It scans `chai`/`chaidemo` source and flags the patterns below. Fold its output into your report. Then spot-check by hand for anything regex misses.

## What counts as a violation (in consumer/screen code)

- **Hardcoded colors** — `Color(0x…)` or `Color.Red`/`Color.White` literals outside `chai/.../atoms/Color.kt` (the one allowed place to define raw color values).
- **Material colorScheme in components** — reading `MaterialTheme.colorScheme.*` instead of `ChaiTheme.colors` / `LocalChaiColorsPalette`.
- **Raw Material components** in screen/demo code where a Chai `C*` equivalent exists (e.g. `androidx.compose.material3.Button` instead of `CButton`/`CPrimaryButton`).
- **Bare `.dp` magic numbers** in component code instead of `Space*` tokens.
- **`@Preview`** instead of `@ChaiPreview`.

## What is allowed (not a violation)

- Material primitives **inside** `C*` component implementations — Chai wraps Material, so the library internals use it by design. The rule targets consumer/screen code and hardcoded values.
- Raw colors defined in `atoms/Color.kt`. That's where they belong.

## Report

Group by violation type. Each: `file:line` + the offending snippet + the Chai replacement. End with a count and a one-line verdict (clean / N violations). Cross-check against `docs/tech/findings.md` so you don't re-report the already-documented `ChaiTheme`/`MaterialTheme.colorScheme` gap as if it were new — note it as "known".
