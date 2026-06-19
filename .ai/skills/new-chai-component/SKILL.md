---
name: new-chai-component
description: |
  Scaffold a new Chai design-system component the right way. Triggered by
  "new-chai-component", "/new-chai-component", "add a Chai component", or
  "create a C<Name>". Ensures tokens, ChaiTheme.colors, and @ChaiPreview
  instead of raw Material / hardcoded values.
---

# new-chai-component

You add a component to the `chai` library so it matches the design system. Components live in `chai/src/main/java/com/droidconke/chai/components/` (or `icons/`, `images/` as appropriate), named `C<Name>` in a `C<Name>s.kt` file when grouping a family.

## Before writing

1. Check it doesn't already exist (`Grep` for the name) and isn't an empty stub already present (`CCards.kt`, `CTabs.kt`, etc. are stubs to fill, not new files to create).
2. Decide the tokens it needs (colors, spacing, shape, typography). If a needed semantic color is missing from `ChaiColors`, add it there first, in both `ChaiLightColorPalette` and `ChaiDarkColorPalette`.

## Rules for the component

- **Colors** come from `ChaiTheme.colors` / `LocalChaiColorsPalette`, NOT `MaterialTheme.colorScheme` and NOT hardcoded `Color(0x…)`. (The current components wrongly read the Material scheme — see `docs/tech/findings.md`; do not copy that pattern.)
- **Spacing** uses `Space*` for `Modifier.padding(...)` and the `Spacer*()` composables (`Spacer5()`, `Spacer15()`, `Spacer30()`) for gaps in rows/columns. Never write a bare `Space5` statement — it's a `Dp`, it renders nothing.
- **Shape** uses `CShapes`. **Type** uses `ChaiTheme.typography`.
- Signature: `onClick`/state first, then `modifier: Modifier = Modifier`, then content. Keep parameters minimal; expose only what consumers need.
- Add **KDoc** on the public composable (it's a published library API).
- Mark internal helpers `private`/`internal`.

## Previews

Add `@ChaiPreview` (not `@Preview`) previews for light and dark, wrapped in `ChaiTheme { }`, and make the preview functions `private`.

```
@ChaiPreview
@Composable
private fun C<Name>Preview() {
    ChaiTheme { C<Name>( … ) }
}
```

## After writing

1. `audit-design-system-usage` skill — confirm no raw Material / hardcoded color slipped in.
2. `checks` skill — lint + tests + assemble.
3. Demo it in `chaidemo` so the component is actually exercised (the demo currently under-covers components).
4. Commit per `commit-convention` (atomic, by layer: token change → component → demo).
