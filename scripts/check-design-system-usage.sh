#!/usr/bin/env bash
# Guard rail: flag source that bypasses the Chai design system.
#
# This is the check the custom `chailinter` is meant to perform but whose
# detectors are currently non-functional stubs (see docs/tech/findings.md).
# Until those are implemented, this script is the enforcement.
#
# HARD FAIL (exit 1):
#   - Hardcoded color literals (`Color(0x…)`, `Color.White`/`Color.Red`/…)
#     anywhere except chai/.../atoms/Color.kt, the one place raw colors are
#     allowed to be defined.
#
# WARN (exit 0, reported):
#   - `MaterialTheme.colorScheme.*` read inside components (should be
#     `ChaiTheme.colors` / `LocalChaiColorsPalette`). Known gap in findings.md.
#   - `@Preview` instead of `@ChaiPreview` (CONTRIBUTING.MD).
#   - Raw Material 3 components imported in chaidemo screen code.
#
# Pass --strict to make warnings fail too.
set -euo pipefail

repo_root="$(git rev-parse --show-toplevel 2>/dev/null)" \
  || repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

strict=0
[ "${1:-}" = "--strict" ] && strict=1

fail=0
warns=0
err()  { echo "FAIL: $*" >&2; fail=1; }
warn() { echo "warn: $*" >&2; warns=$((warns + 1)); [ "$strict" -eq 1 ] && fail=1; return 0; }

# Source roots to scan (Kotlin only).
roots=(chai/src/main chaidemo/src/main)
allow_color_file="chai/src/main/java/com/droidconke/chai/atoms/Color.kt"

kt_files() { # list .kt under the roots that exist
  for r in "${roots[@]}"; do
    [ -d "$r" ] && find "$r" -type f -name '*.kt'
  done
}

# --- HARD: hardcoded colors outside the atoms color file -----------------
while IFS= read -r f; do
  [ -n "$f" ] || continue
  [ "$f" = "$allow_color_file" ] && continue
  hits="$(grep -nE 'Color\(0x|Color\.(White|Black|Red|Green|Blue|Gray|Grey|Yellow|Cyan|Magenta|Transparent|DarkGray|LightGray)' "$f" || true)"
  if [ -n "$hits" ]; then
    while IFS= read -r line; do
      err "$f:${line%%:*} hardcoded color — define it in atoms/Color.kt and use a Chai token"
    done <<< "$hits"
  fi
done < <(kt_files)

# --- WARN: MaterialTheme.colorScheme in components -----------------------
while IFS= read -r f; do
  [ -n "$f" ] || continue
  hits="$(grep -nE 'MaterialTheme\.colorScheme' "$f" || true)"
  if [ -n "$hits" ]; then
    while IFS= read -r line; do
      warn "$f:${line%%:*} reads MaterialTheme.colorScheme — use ChaiTheme.colors / LocalChaiColorsPalette"
    done <<< "$hits"
  fi
done < <(kt_files)

# --- WARN: @Preview instead of @ChaiPreview ------------------------------
while IFS= read -r f; do
  [ -n "$f" ] || continue
  # match @Preview but not @ChaiPreview
  hits="$(grep -nE '@Preview' "$f" | grep -v 'ChaiPreview' || true)"
  if [ -n "$hits" ]; then
    while IFS= read -r line; do
      warn "$f:${line%%:*} uses @Preview — prefer @ChaiPreview (CONTRIBUTING.MD)"
    done <<< "$hits"
  fi
done < <(kt_files)

# --- WARN: raw Material 3 components in chaidemo screens ------------------
if [ -d chaidemo/src/main ]; then
  while IFS= read -r f; do
    [ -n "$f" ] || continue
    hits="$(grep -nE 'androidx\.compose\.material3\.(Button|OutlinedButton|Card|Text|TabRow|Tab|TextField|OutlinedTextField)\b' "$f" || true)"
    if [ -n "$hits" ]; then
      while IFS= read -r line; do
        warn "$f:${line%%:*} imports a raw Material 3 component in demo code — use the Chai C* equivalent"
      done <<< "$hits"
    fi
  done < <(find chaidemo/src/main -type f -name '*.kt')
fi

echo
if [ "$fail" -ne 0 ]; then
  echo "design-system check FAILED ($warns warning(s))"; exit 1
fi
echo "design-system check passed ($warns warning(s))"
