#!/usr/bin/env bash
# Validate documentation (.md) — dead links, filename hygiene, staleness dates.
#
# Docs-only changes skip the Gradle checks, so without this a broken README link
# (the kind docs/tech/findings.md flagged) reaches the branch unvalidated. Fast:
# shell + python3, plus markdownlint-cli2 when installed. Checks only the files
# you changed, so legacy docs never block you.
#
# Usage:
#   scripts/check-docs.sh [file.md ...]   # check the given files
#   scripts/check-docs.sh                 # check .md changed vs origin/develop
#
# Checks (BLOCK on failure; markdownlint degrades to a warning when absent):
#   1. Dead relative links — [text](path) pointing at a missing file.
#   2. Filename hygiene — no spaces in .md filenames.
#   3. Content hygiene — no "Last Updated" lines (docs carry no staleness dates).
#   4. markdownlint-cli2 — formatting, if installed.
#   5. Agent tooling — front-matter + MEMORY.md index, via check-agent-tooling.sh.
set -euo pipefail

root="$(git rev-parse --show-toplevel)"
cd "$root"

base_ref="origin/develop"
git rev-parse --verify -q "$base_ref" >/dev/null || base_ref="origin/main"

files=()
if [ "$#" -gt 0 ]; then
  for f in "$@"; do [[ "$f" == *.md ]] && files+=("$f"); done
else
  base="$(git merge-base HEAD "$base_ref" 2>/dev/null || true)"
  if [ -n "$base" ]; then
    while IFS= read -r f; do [ -n "$f" ] && files+=("$f"); done \
      < <(git diff --name-only --diff-filter=ACMR "$base" HEAD -- '*.md')
  fi
fi

# Keep only files that still exist.
existing=()
for f in "${files[@]:-}"; do [ -f "$f" ] && existing+=("$f"); done

fail=0
err()  { echo "FAIL: $*" >&2; fail=1; }
warn() { echo "warn: $*" >&2; }

if [ "${#existing[@]}" -eq 0 ]; then
  echo "info: no changed .md files to check"
else
  for f in "${existing[@]}"; do
    dir="$(dirname "$f")"

    # 1. dead relative links — [text](path), skipping URLs and anchors.
    while IFS= read -r target; do
      [ -n "$target" ] || continue
      case "$target" in
        http://*|https://*|mailto:*|\#*) continue ;;
      esac
      clean="${target%%#*}"          # strip anchor
      [ -z "$clean" ] && continue
      case "$clean" in
        /*) resolved=".$clean" ;;     # repo-absolute
        *)  resolved="$dir/$clean" ;; # relative to the doc
      esac
      [ -e "$resolved" ] || err "$f: dead link -> $target"
    done < <(grep -oE '\]\(([^)]+)\)' "$f" | sed -E 's/^\]\(//; s/\)$//')

    # 2. filename hygiene
    case "$(basename "$f")" in
      *" "*) err "$f: filename contains spaces" ;;
    esac

    # 3. staleness dates
    grep -qiE '^\s*(_|\*\*)?last updated' "$f" && err "$f: contains a 'Last Updated' line (docs carry no staleness dates)"
  done

  # 4. markdownlint — advisory only (legacy docs don't conform; don't block).
  #    Tuned by .markdownlint.jsonc. Reports, never fails the gate.
  if command -v markdownlint-cli2 >/dev/null 2>&1; then
    markdownlint-cli2 "${existing[@]}" >/dev/null 2>&1 || warn "markdownlint reported formatting issues (advisory) — run: markdownlint-cli2 ${existing[*]}"
  else
    warn "markdownlint-cli2 not installed — skipping format lint"
  fi
fi

# 5. agent tooling
if [ -x scripts/check-agent-tooling.sh ]; then
  ./scripts/check-agent-tooling.sh >/dev/null || err "check-agent-tooling failed"
fi

echo
if [ "$fail" -ne 0 ]; then echo "check-docs FAILED"; exit 1; fi
echo "check-docs passed"
