#!/usr/bin/env bash
# Install this repo's committed git hooks (.githooks/) by pointing git at them.
# Opt-in and idempotent — run once after cloning. Changes git config for THIS
# repo only.
#
# Sets repo-local core.hooksPath=.githooks. That overrides any global
# core.hooksPath for this repo, so the .githooks/commit-msg shim chains back to
# a global commit-msg hook to preserve its behavior (e.g. message validation).
#
# Hooks installed:
#   commit-msg  — chains to your global commit-msg hook (format validation)
#   pre-commit  — agent-tooling + design-system checks; check-docs on staged .md
#   pre-push    — lintDebug + testDebugUnitTest + assembleDebug (blocking)
# Bypass once: SKIP_LOCAL_CI=1 git commit/push
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"

[ -d .githooks ] || { echo "error: .githooks/ not found at $root" >&2; exit 1; }

chmod +x .githooks/* 2>/dev/null || true

prev="$(git config --local core.hooksPath || true)"
git config core.hooksPath .githooks

echo "ok: core.hooksPath -> .githooks (was: ${prev:-unset})"
echo "    pre-commit + pre-push now run the repo guard rails; commit-msg chains to any global hook."
echo "    bypass once with: SKIP_LOCAL_CI=1 git commit/push"
