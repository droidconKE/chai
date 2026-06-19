# Agents

Tool-neutral guidance for any AI agent (Claude Code, Cursor, Copilot, Gemini, etc.) working in the Chai repo. This is the propagation point: every tool that imports an agent-instructions file should be pointed here.

Chai is the **droidconKE design system** — a Jetpack Compose component library plus a demo app and a custom lint module. It is a library others depend on, so API surface and visual consistency matter more than feature velocity.

## Where things live

| Path | What it is | Committed? |
|------|------------|------------|
| `.ai/skills/` | Reusable task procedures (`SKILL.md` each) | yes |
| `.ai/agents/` | Role briefs for sub-agents | yes |
| `.ai/memory/` | Per-user, tool-neutral knowledge store | **no** (git-ignored) |
| `scripts/` | Guard-rail scripts (run in CI and locally) | yes |
| `docs/` | Human docs; `docs/tech/findings.md` is the current code-health baseline | yes |

## Before you start a task

1. **Read `.ai/memory/MEMORY.md`** (the index) and open any entry that looks relevant. This is where decisions, corrections, and project state are kept. If the directory is absent, the project has no local memory yet.
2. **Check `.ai/skills/`** — if a skill covers the task (build check, new component, commit message, PR), follow it rather than improvising.
3. **Read `docs/tech/findings.md`** for known gaps before "fixing" something that is already documented as a stub.

## Non-negotiables for this repo

- **Design system only.** In `chai`/`chaidemo` source, use Chai components and tokens (`ChaiTheme`, `ChaiColors`, the `C*` components, `Space*`/`Spacer*`). Never raw Material 3 components or hardcoded `Color(0x…)` / `.dp` literals in component code. `scripts/check-design-system-usage.sh` enforces this.
- **Compose previews** use `@ChaiPreview`, not `@Preview` (see `CONTRIBUTING.MD`).
- **Branches** target `develop`, not `main`. Prefixes: `feature/`, `fix/`, `docs/`.
- **Commits**: past tense, concise subject, no period, no body, no conventional-commits prefix. ("Added X", not "feat: add x.")
- **Never** `git push --no-verify` or `--force`. Fix hook failures instead.

## Memory

When you learn something durable (a decision + why, a correction, project state, an external pointer), save it to `.ai/memory/` following `.ai/memory/reference_memory_store_convention.md`, and add a one-line bullet to `MEMORY.md`. Use the `memory-curator` agent for this. The store is git-ignored and per-user, so it is never committed.
