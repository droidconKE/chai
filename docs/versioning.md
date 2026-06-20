# Versioning

Chai follows [Semantic Versioning](https://semver.org/). Releases are tagged on `main`
as `vMAJOR.MINOR.PATCH` (for example `v1.2.0`).

## What `v1.2.0` means

It's three numbers, `MAJOR.MINOR.PATCH`, and the `v` is just a conventional prefix for the git tag:

```
v 1 . 2 . 0
  │   │   └── PATCH — backward-compatible bug fixes only
  │   └────── MINOR — new functionality, backward-compatible (nothing breaks)
  └────────── MAJOR — breaking/incompatible API changes
```

The rule is "bump the leftmost number that applies, and reset the ones to its right to 0":

| Change | From → To |
| --- | --- |
| Fix a bug, no API change | `1.2.0` → `1.2.1` |
| Add a new component, old code still works | `1.2.1` → `1.3.0` |
| Rename/remove a public API (breaking) | `1.3.0` → `2.0.0` |

## What this maps to for Chai

For a design-system library, the bump maps cleanly to consumer impact:

- **PATCH** — fix `CPrimaryButton`'s disabled color.
- **MINOR** — ship the new `CCards` / `CTabs` components (additive).
- **MAJOR** — the `ChaiSteal` → `ChaiSteel` rename, or dropping a public color token
  (breaks anyone depending on it).

## Two conventions

- **`0.x.y`** means "pre-1.0, anything can change" — useful while the API is still settling.
  Cut `v1.0.0` once the public API is committed to being stable.
- A **pre-release suffix** like `v1.3.0-alpha01` or `-rc1` marks a not-yet-final build
  (the same scheme AGP uses).

## Release flow

`develop` is the integration branch; `main` is the release line.

1. Day-to-day work merges into `develop` via PRs.
2. To cut a release, open a PR from `develop` into `main`.
3. Once it merges, tag the merge commit on `main` with `vMAJOR.MINOR.PATCH`.

The tag on `main` is the release. Only release-ready code reaches `main`.
