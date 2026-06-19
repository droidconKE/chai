# Git hooks

Committed, opt-in git hooks that run the repo's guard rails locally before code reaches CI. Activate them once per clone:

```bash
./scripts/install-git-hooks.sh
```

That points this repo's `core.hooksPath` at `.githooks/`. It is opt-in (nothing runs until you install) and repo-local (your global git config is untouched).

## What runs

| Hook | When | Runs | Speed |
|------|------|------|-------|
| `commit-msg` | every commit | chains to your global commit-msg hook (message validation) | instant |
| `pre-commit` | every commit | `check-agent-tooling.sh`, `check-design-system-usage.sh`, and `check-docs.sh` on staged `.md` | < 1s |
| `pre-push` | every push | `./gradlew lintDebug testDebugUnitTest assembleDebug` | minutes |

## Bypass

Emergencies only. Never bypass to dodge a real failure:

```bash
SKIP_LOCAL_CI=1 git commit …
SKIP_LOCAL_CI=1 git push
```

`git commit --no-verify` / `git push --no-verify` and `--force` are not allowed by repo convention; fix the failure instead.

## Note on commit-msg

Installing repo hooks overrides any global `core.hooksPath`, so `commit-msg` here is a shim that re-invokes your global commit-msg hook (the one that validates subject format). If you have no global hook, it exits cleanly.
