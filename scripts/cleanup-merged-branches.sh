#!/usr/bin/env bash
# Purge local branches whose work has already landed on the base branch.
#
# A branch is "merged" if EITHER:
#   (a) it is reachable from <base> (git branch --merged — true merge / FF), OR
#   (b) its PR is MERGED on GitHub (covers squash-merges, whose commits are never
#       reachable from <base>).
#
# Always protected: main, develop, the current branch, <base>, and any branch
# checked out in another worktree. Squash-merged branches need force-delete
# (-D); safe because we verify the PR is MERGED and its merged head SHA matches
# the local tip — a reused name with new commits is kept.
#
# Dry-run by default. Pass --apply to delete. `gh` (authenticated) enables the
# squash-merge PR check; without it, only reachable-merged branches are eligible.
set -euo pipefail

base="develop"
apply=false
for arg in "$@"; do
  case "$arg" in
    --apply) apply=true ;;
    --base=*) base="${arg#*=}" ;;
    -h|--help) grep '^#' "$0" | grep -v '^#!' | sed 's/^# \{0,1\}//'; exit 0 ;;
    *) echo "unknown arg: $arg" >&2; exit 2 ;;
  esac
done

cd "$(git rev-parse --show-toplevel)"

echo "Fetching and pruning origin…"
git fetch --prune origin >/dev/null 2>&1 || git fetch --prune origin

# Resolve base to a ref that exists: prefer a local branch, fall back to origin/.
if ! git rev-parse --verify -q "$base" >/dev/null; then
  if git rev-parse --verify -q "origin/$base" >/dev/null; then
    base="origin/$base"
  else
    echo "error: base '$base' not found locally or on origin" >&2; exit 1
  fi
fi

current="$(git branch --show-current)"
protected="main develop $base"
is_protected() { printf '%s\n' $protected | grep -qx "$1"; }

have_gh=false
if command -v gh >/dev/null 2>&1 && gh auth status >/dev/null 2>&1; then
  have_gh=true
else
  echo "warn: gh unavailable/unauthenticated — only reachable-merged branches are eligible." >&2
fi

worktree_branches="$(git worktree list --porcelain | awk '/^branch /{sub("refs/heads/","",$2); print $2}')"
is_worktree() { printf '%s\n' "$worktree_branches" | grep -qx "$1"; }

reachable_merged="$(git branch --merged "$base" --format='%(refname:short)')"
is_reachable_merged() { printf '%s\n' "$reachable_merged" | grep -qx "$1"; }

del_safe=(); del_force=(); skipped=()

while IFS= read -r b; do
  [ -n "$b" ] || continue
  if is_protected "$b"; then skipped+=("$b (protected)"); continue; fi
  if [ "$b" = "$current" ]; then skipped+=("$b (current branch)"); continue; fi
  if is_worktree "$b"; then skipped+=("$b (checked out in a worktree)"); continue; fi

  if is_reachable_merged "$b"; then del_safe+=("$b"); continue; fi

  if $have_gh; then
    local_tip="$(git rev-parse --verify "$b" 2>/dev/null || true)"
    match="$(gh pr list --head "$b" --state merged --json number,headRefOid \
      --jq ".[] | select(.headRefOid == \"$local_tip\") | .number" 2>/dev/null | head -n1 || true)"
    if [ -n "$match" ]; then del_force+=("$b (PR #$match merged)"); continue; fi
    stale="$(gh pr list --head "$b" --state merged --json number --jq '.[0].number' 2>/dev/null || true)"
    if [ -n "$stale" ]; then skipped+=("$b (PR #$stale merged but local tip differs — kept)"); continue; fi
  fi
  skipped+=("$b (not merged — kept)")
done < <(git branch --format='%(refname:short)')

echo
echo "Base: $base   Current: $current   Protected: $protected"
echo "── Will delete (reachable-merged) ──"; printf '  %s\n' "${del_safe[@]:-(none)}"
echo "── Will delete (squash-merged PR, force) ──"; printf '  %s\n' "${del_force[@]:-(none)}"
echo "── Skipped ──"; printf '  %s\n' "${skipped[@]:-(none)}"
echo

if ! $apply; then
  echo "Dry run. Re-run with --apply to delete the branches above."
  exit 0
fi

for b in "${del_safe[@]:-}"; do [ -n "$b" ] && git branch -d "$b"; done
for entry in "${del_force[@]:-}"; do [ -n "$entry" ] && git branch -D "${entry%% *}"; done
echo "Done."
