#!/usr/bin/env bash
# Lint the repo's shared AI tooling (.ai/) for drift. Safe to run in CI.
#
# Checks (committed surfaces — always run):
#   1.  Every .ai/skills/<dir> has a SKILL.md with `name:` and `description:`
#       front-matter, and the front-matter `name` matches the directory.
#   1b. Every .ai/agents/<name>.md has `name:` and `description:` front-matter,
#       and the front-matter `name` matches the filename stem.
#
# Checks (local-only — .ai/memory/ is git-ignored, absent in CI):
#   2.  Every .ai/memory/*.md (except MEMORY.md) is linked from MEMORY.md, and
#       MEMORY.md has no bullets pointing at a missing file.
#
# Exits non-zero if any committed-surface check fails. Memory drift is a
# warning when the store is present, and skipped entirely when it isn't.
set -euo pipefail

repo_root="$(git rev-parse --show-toplevel 2>/dev/null)" \
  || repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

fail=0
err()  { echo "FAIL: $*" >&2; fail=1; }
warn() { echo "warn: $*" >&2; }
ok()   { echo "ok:   $*"; }

fm_field() { # file, field -> first value inside the leading --- block
  sed -n '/^---$/,/^---$/p' "$1" | sed -n "s/^$2:[[:space:]]*//p" | head -1
}

# --- 1. SKILL.md front-matter --------------------------------------------
skill_count=0
if [ -d .ai/skills ]; then
  for d in .ai/skills/*/; do
    [ -d "$d" ] || continue
    name="$(basename "$d")"
    f="${d}SKILL.md"
    if [ ! -f "$f" ]; then err "$d has no SKILL.md"; continue; fi
    [ -n "$(fm_field "$f" name)" ] || err "$f: missing front-matter 'name:'"
    [ -n "$(fm_field "$f" description)" ] || err "$f: missing front-matter 'description:'"
    fm_name="$(fm_field "$f" name)"
    if [ -n "$fm_name" ] && [ "$fm_name" != "$name" ]; then
      err "$f: front-matter name '$fm_name' != directory '$name'"
    fi
    skill_count=$((skill_count + 1))
  done
fi
[ "$skill_count" -gt 0 ] && ok "validated $skill_count SKILL.md front-matters"

# --- 1b. Agent role-brief front-matter -----------------------------------
agent_count=0
if [ -d .ai/agents ]; then
  for f in .ai/agents/*.md; do
    [ -f "$f" ] || continue
    stem="$(basename "$f" .md)"
    [ -n "$(fm_field "$f" name)" ] || err "$f: missing front-matter 'name:'"
    [ -n "$(fm_field "$f" description)" ] || err "$f: missing front-matter 'description:'"
    fm_name="$(fm_field "$f" name)"
    if [ -n "$fm_name" ] && [ "$fm_name" != "$stem" ]; then
      err "$f: front-matter name '$fm_name' != filename '$stem'"
    fi
    agent_count=$((agent_count + 1))
  done
fi
[ "$agent_count" -gt 0 ] && ok "validated $agent_count agent role-brief front-matters"

# --- 2. Memory index integrity (local-only) ------------------------------
mem_dir=".ai/memory"
index="$mem_dir/MEMORY.md"
if [ -d "$mem_dir" ] && [ -f "$index" ]; then
  for f in "$mem_dir"/*.md; do
    base="$(basename "$f")"
    [ "$base" = "MEMORY.md" ] && continue
    grep -q "($base)" "$index" || warn "memory file not indexed in MEMORY.md: $base"
  done
  grep -oE '\(([a-zA-Z0-9_./-]+\.md)\)' "$index" | tr -d '()' | while IFS= read -r ref; do
    [ -f "$mem_dir/$ref" ] || warn "MEMORY.md links missing file: $ref"
  done
  ok "memory index checked (local store present)"
else
  echo "info: .ai/memory absent (git-ignored / CI) — skipping memory checks"
fi

if [ "$fail" -ne 0 ]; then
  echo; echo "agent-tooling check FAILED"; exit 1
fi
echo; echo "agent-tooling check passed"
