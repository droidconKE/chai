#!/usr/bin/env bash
# Tool-neutral PR / branch review helper. No provider, no API key required.
#
# Bundles the three-dot diff (<base>...<head>) plus the committed review brief
# (.ai/agents/code-reviewer.md) into one prompt, then EITHER:
#   - prints it to stdout (default) so you paste it into any agent, or
#   - pipes it on stdin to an engine command you choose, e.g.:
#       ./scripts/ai-review.sh --engine='claude -p'
#       AI_REVIEW_CMD='claude -p' ./scripts/ai-review.sh
#
# The engine is your choice — this script hardcodes none, so it works with
# whichever agent CLI you have authenticated locally.
#
# Targets (default: current branch vs origin/develop):
#   --pr=N           review GitHub PR #N           (needs gh)
#   --branch=NAME    review local branch NAME
#   --base=NAME      base to diff against           (default: develop)
#   --fetch          refresh the base from origin first (off by default so the
#                    script never blocks on the network; uses local origin/<base>)
set -euo pipefail

base="develop"; pr=""; branch=""; engine="${AI_REVIEW_CMD:-}"; do_fetch=0
for arg in "$@"; do
  case "$arg" in
    --base=*)   base="${arg#*=}" ;;
    --pr=*)     pr="${arg#*=}" ;;
    --branch=*) branch="${arg#*=}" ;;
    --engine=*) engine="${arg#*=}" ;;
    --fetch)    do_fetch=1 ;;
    --pr|--branch|--base|--engine)
      echo "use ${arg}=VALUE (with '=')" >&2; exit 2 ;;
    -h|--help)
      grep '^#' "$0" | grep -v '^#!' | sed 's/^# \{0,1\}//'; exit 0 ;;
    *) echo "unknown arg: $arg" >&2; exit 2 ;;
  esac
done

cd "$(git rev-parse --show-toplevel)"
brief=".ai/agents/code-reviewer.md"
[ -f "$brief" ] || { echo "error: $brief not found" >&2; exit 1; }

if [ -n "$pr" ]; then
  command -v gh >/dev/null 2>&1 || { echo "error: --pr needs gh" >&2; exit 1; }
  header="GitHub PR #$pr"
  diff="$(gh pr diff "$pr")"
  files="$(gh pr diff "$pr" --name-only)"
else
  head="${branch:-HEAD}"
  label="${branch:-$(git branch --show-current)}"
  [ "$do_fetch" -eq 1 ] && { git fetch -q origin "$base" 2>/dev/null || true; }
  ref="$base"; git rev-parse --verify -q "origin/$base" >/dev/null && ref="origin/$base"
  header="branch '$label' vs $ref"
  diff="$(git diff "$ref...$head")"
  files="$(git diff --name-only "$ref...$head")"
fi

if [ -z "$diff" ]; then
  echo "No changes to review for $header." >&2; exit 0
fi

prompt="$(cat <<EOF
$(cat "$brief")

---
## This review

Target: $header

Changed files:
$files

Unified diff (review the changes below against the brief above):

\`\`\`diff
$diff
\`\`\`
EOF
)"

if [ -n "$engine" ]; then
  echo "Running review via: $engine" >&2
  printf '%s' "$prompt" | eval "$engine"
else
  printf '%s\n' "$prompt"
fi
