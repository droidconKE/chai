---
name: commit-convention
description: |
  Produce a commit subject, branch name, and PR title that match Chai's
  conventions. Triggered by "commit-convention", "/commit-convention",
  "write a commit message", or by the gitter agent before committing or
  opening a PR.
---

# commit-convention

You turn a set of changes into a correctly-formatted commit subject, branch name, and PR title. You draft text; the `gitter` agent or the user runs the git commands.

## Commit subject rules (a shared commit-msg hook enforces these)

- **Past tense**, concise, describes the change.
- **No trailing period.** No body. No conventional-commits prefix (`feat:`, `fix:`).
- Keep it short (aim < 72 chars).
- No AI/agent `Co-Authored-By` trailer.

Good: `Added CCard elevation token` · `Fixed primary button container color` · `Updated dependencies to latest versions`
Bad: `feat: add card` · `Fixes the button.` · `update stuff`

## Atomic commits

1–3 files, split by dependency layer: tokens/atoms → components → theme wiring → demo → tests → docs. **One documentation `.md` file per commit** — never bundle multiple docs. If the change spans layers, propose multiple commits with a subject each.

## Branch names

`feature/<slug>`, `fix/<slug>`, or `docs/<slug>` — lowercase, hyphenated. Example: `feature/ccard-elevation`.

## PR title

Same style as the commit subject (past tense, no prefix/period). PRs target **`develop`**.

## Output

```
Branch:  feature/<slug>
Commits:
  1. <subject>            (files: …)
  2. <subject>            (files: …)
PR title: <title>   (base: develop)
```

If the staged/working changes don't justify splitting, output a single commit. Read the actual diff (`git diff`) before drafting — base the subject on what changed, not on the request wording.
