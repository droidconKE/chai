# .ai

Tool-neutral AI infrastructure for the Chai design system repo. Plain Markdown and shell — no tool-specific config, so Claude Code, Cursor, Copilot, Gemini, and others can all read it. The repo-root `AGENTS.md` points agents here.

## Layout

```
.ai/
  agents/   role briefs for sub-agents (committed)
  skills/   reusable task procedures, one SKILL.md per dir (committed)
  memory/   per-user knowledge store, git-ignored (NOT committed)
  tmp/      scratch space, git-ignored
```

Guard-rail scripts live in the repo's `scripts/` dir (committed, run in CI and from git hooks):

- `scripts/check-agent-tooling.sh` — validates this directory: every skill/agent has correct front-matter, and the memory index has no dangling links.
- `scripts/check-design-system-usage.sh` — fails when component source uses raw Material components or hardcoded colors instead of Chai tokens.

## What each part is for

| Part | Use it when |
|------|-------------|
| `agents/` | You want a focused role (review, planning, git, memory) with its own instructions. |
| `skills/` | A task has a repeatable procedure (run checks, scaffold a component, write a commit). Follow the SKILL.md instead of improvising. |
| `memory/` | You learned something durable (a decision + why, a correction, project state). Save it so the next session starts informed. |

## Conventions

- **Skills**: each `.ai/skills/<slug>/SKILL.md` has YAML front-matter with `name` (must equal the directory) and `description` (one line on *when* to invoke).
- **Agents**: each `.ai/agents/<name>.md` has front-matter with `name` (must equal the filename stem) and `description`.
- **Memory**: one fact per file, typed (`user`/`feedback`/`project`/`reference`), indexed in `MEMORY.md`. See `memory/reference_memory_store_convention.md`.

Run `scripts/check-agent-tooling.sh` after editing anything here.
