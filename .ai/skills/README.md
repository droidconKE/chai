# Skills

Reusable, tool-agnostic procedures for the Chai design system repo. Each subdirectory has a `SKILL.md` with YAML front-matter (`name`, `description`). Any agent can follow them; they are self-contained Markdown with no tool-specific wiring.

## Available skills

| Skill | What it does |
|-------|--------------|
| [`checks`](./checks/SKILL.md) | Umbrella pre-PR gate: design-system audit → lint → unit tests → assemble. Run before every commit/PR. |
| [`check-build`](./check-build/SKILL.md) | Verify the project assembles (`./gradlew assembleDebug`). Reports only. |
| [`check-lint`](./check-lint/SKILL.md) | Run Android Lint (`./gradlew lintDebug`) and report the first violation. No auto-fix. |
| [`commit-convention`](./commit-convention/SKILL.md) | Produce a commit subject + branch name + PR title that match the repo conventions. |
| [`new-chai-component`](./new-chai-component/SKILL.md) | Scaffold a new design-system component the Chai way (tokens, `ChaiTheme.colors`, `@ChaiPreview`). |
| [`audit-design-system-usage`](./audit-design-system-usage/SKILL.md) | Find raw Material usage and hardcoded colors that should be Chai tokens. The check the broken `chailinter` is meant to do. |
| [`dependency-bump`](./dependency-bump/SKILL.md) | Update `gradle/libs.versions.toml` to latest and verify, per the `findings.md` checklist. |

## Authoring a new skill

```
.ai/skills/<slug>/
  SKILL.md      # required; YAML front-matter + instructions
  scripts/      # optional helpers
  templates/    # optional file templates
```

Front-matter (keep `description` to one sentence on *when* to invoke):

```yaml
---
name: my-skill
description: One sentence describing WHEN to use this skill.
---
```

`name` must equal the directory name. After adding a skill, run `scripts/check-agent-tooling.sh` and add a row to the table above.
