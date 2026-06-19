#!/usr/bin/env bash
# Tool-neutral guard: keep `gh pr create` honest for the Chai repo.
#
# ONE implementation; every tool's hook config points here. The blocking
# "dialect" is chosen with --format:
#   Claude Code : PreToolUse(Bash)       -> ... --format claude   (stderr + exit 2)
#   Cursor      : beforeShellExecution   -> ... --format cursor   (JSON permission)
#   other/exit  : any pre-shell hook     -> ... --format exit     (default)
#
# Reads the tool's hook payload (JSON) on stdin, finds the shell command, and
# blocks a `gh pr create` unless it targets `develop` and carries a body (not an
# empty/inline-only body). Fails open on anything it cannot parse.
#
# Chai rule: PRs target develop, link an issue, and describe how to test
# (see .github/pull_request_template.md and CONTRIBUTING.MD).
set -uo pipefail

format="exit"
case "${1:-}" in
  --format)   format="${2:-exit}" ;;
  --format=*) format="${1#--format=}" ;;
esac

input="$(cat)"

cmd="$(printf '%s' "$input" | python3 -c '
import json,sys
try:
    d = json.load(sys.stdin)
except Exception:
    print(""); sys.exit(0)
c = (d.get("tool_input") or {}).get("command") or d.get("command") or ""
print(c)
' 2>/dev/null || true)"

help_text() {
  cat <<EOF
$1

Chai PRs must target develop and follow the repo template. Build it like:

  gh pr create --base develop \\
    --title "<past-tense summary>" \\
    --body-file <file>      # body from .github/pull_request_template.md

The body must reference the issue ("Fixes #N") and say how to test the change.
See .ai/skills/commit-convention/SKILL.md and the gitter agent.
EOF
}

allow() {
  [ "$format" = "cursor" ] && printf '{"permission":"allow"}\n'
  exit 0
}

deny() {
  local short="$1" full
  full="$(help_text "BLOCKED: $short")"
  case "$format" in
    cursor)
      python3 - "$short" "$full" <<'PY'
import json,sys
print(json.dumps({"permission":"deny","userMessage":sys.argv[1],"agentMessage":sys.argv[2]}))
PY
      exit 0 ;;
    *)  printf '%s\n' "$full" >&2; exit 2 ;;
  esac
}

# Only police a real `gh pr create` at a command boundary.
printf '%s' "$cmd" \
  | grep -Eq '(^|[;&|(])[[:space:]]*gh[[:space:]]+pr[[:space:]]+create([[:space:]]|$)' \
  || allow

# 1. Must target develop. Missing --base defaults to the repo default (main) — block.
base="$(printf '%s' "$cmd" | sed -nE 's/.*--base[[:space:]=]+"?([^"[:space:]]+).*/\1/p' | head -1)"
[ -n "$base" ] || deny "no --base given; Chai PRs must use --base develop (default would target main)."
[ "$base" = "develop" ] || deny "PR base is '$base'; Chai PRs target develop."

# 2. Must carry a non-empty body (file or inline). Empty body is a block.
if printf '%s' "$cmd" | grep -Eq -- '(^|[[:space:]])--body-file([[:space:]]|=)'; then
  bodyfile="$(printf '%s' "$cmd" | sed -nE 's/.*--body-file[[:space:]=]+"?([^"[:space:]]+).*/\1/p' | head -1)"
  if [ -n "${bodyfile:-}" ] && [ -f "$bodyfile" ]; then
    grep -qiE 'fixes #|issues?|#[0-9]+' "$bodyfile" \
      || deny "PR body '$bodyfile' does not reference an issue (e.g. 'Fixes #123')."
  fi
elif printf '%s' "$cmd" | grep -Eq -- '(^|[[:space:]])--body([[:space:]]|=)'; then
  : # inline body present — allowed
else
  deny "PR has no body; add --body-file (the template) or --body."
fi

allow
