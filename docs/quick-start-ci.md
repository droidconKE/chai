# Quick Start: CI/CD Setup

## 🚀 TL;DR


## Running Builds Locally

### From Project Root

**Important:** Always run Gradle commands from the project root (where `gradlew` is located):

```bash
# You should be here:
cd /path/to/chai

# NOT here:
# cd /path/to/chai/docs  ❌
```

### Test the Build

```bash
# Clean build with configuration cache (matches CI)
./gradlew clean build --configuration-cache

# Lint
./gradlew lintDebug --build-cache --parallel --configure-on-demand --configuration-cache

# Unit tests
./gradlew testDebugUnitTest --build-cache --parallel --configure-on-demand --configuration-cache

# Instrumented tests (requires emulator)
./gradlew connectedDebugAndroidTest --build-cache --parallel --configure-on-demand --configuration-cache
```

---

## Understanding CI Jobs

### What Runs on Every PR/Push?

```
cancel-previous (15s)
    ↓
    ├─ lint (1m 20s → 40s cached)
    ├─ build (1m 40s → 50s cached)  } PARALLEL
    └─ test (50s → 30s cached)
```

**Total time:** ~2m 25s first run, ~50s cached ⚡

### What Runs Conditionally?

**Instrumented Tests** only run when:

1. Pushing to `main` or `develop` branches, OR
2. Commit message contains `[test-instrumented]`

**To trigger instrumented tests on your PR:**

```bash
git commit -m "Add new feature [test-instrumented]"
git push
```

---

## CI Optimization Features

### ✅ Enabled by Default

- **4 parallel workers** (utilizing all 4 GitHub runner cores)
- **Configuration cache** (caches Gradle configuration phase)
- **Build cache** (reuses task outputs)
- **Incremental compilation** (only recompiles changed files)
- **4GB JVM heap** (faster builds with more memory)
- **Parallel GC** (better garbage collection for CI)

### 📊 Performance Impact

| Scenario | Time | vs Sequential | vs No Cache |
|----------|------|---------------|-------------|
| First run (no cache) | 2m 25s | 48% faster | baseline |
| Cached build | 50s | 48% faster | **76% faster** 🔥 |
| Instrumented tests | 8-9m | N/A | 20% faster |

---

## Common Tasks

### Running Just One Module

```bash
# Test only chai library
./gradlew :chai:test

# Build only chaidemo app
./gradlew :chaidemo:assembleDebug
```

### Checking Configuration Cache

```bash
# Verify configuration cache works
./gradlew build --configuration-cache

# See configuration cache report
./gradlew build --configuration-cache --info | grep "configuration cache"
```

### Debugging Slow Builds

```bash
# Profile build performance (generates HTML report)
./gradlew build --profile

# Check task dependencies
./gradlew :chai:assembleDebug --dry-run

# Profile build performance
./gradlew build --profile
```

---

## Troubleshooting

### "Configuration cache entry reused" not showing

**Cause:** First run or files changed  
**Solution:** Run build twice - first populates cache, second uses it

```bash
./gradlew clean build --configuration-cache  # Populates cache
./gradlew clean build --configuration-cache  # Uses cache ✅
```

### Build failing with "Project directory not part of build"

**Cause:** Running Gradle from wrong directory  
**Solution:** Navigate to project root first

```bash
# Wrong
cd docs
./gradlew build  # ❌ Error

# Correct
cd /path/to/chai  # Project root
./gradlew build   # ✅ Works
```

### Configuration cache problems

**Cause:** Plugin incompatibility  
**Solution:** Temporarily disable or report issue

```bash
# Disable configuration cache temporarily
./gradlew build --no-configuration-cache

# Check compatibility
./gradlew help --configuration-cache
```

### Out of memory errors

**Cause:** Not enough JVM heap  
**Solution:** Increase memory in `gradle.properties`

```properties
org.gradle.jvmargs=-Xmx6144m  # Increase to 6GB
```

---

## What Gets Cached?

### 1. Gradle Dependencies Cache

- **What:** Downloaded dependencies (AAR, JAR files)
- **Where:** `~/.gradle/caches/modules-2/`
- **Invalidation:** When `libs.versions.toml` or `build.gradle.kts` changes
- **Speed boost:** 30-40% on dependency resolution

### 2. Configuration Cache

- **What:** Gradle configuration phase results
- **Where:** `.gradle/configuration-cache/`
- **Invalidation:** When build scripts change
- **Speed boost:** 30-50% on configuration phase

### 3. Build Cache

- **What:** Task output results
- **Where:** `.gradle/build-cache/`
- **Invalidation:** When task inputs change
- **Speed boost:** 20-40% on compilation

### 4. AVD Cache (CI only)

- **What:** Android Virtual Device snapshot
- **Where:** `~/.android/avd/`
- **Invalidation:** When API level changes
- **Speed boost:** 3-5 minutes saved per instrumented test run

---

## Best Practices

### ✅ Do

- Run builds from project root
- Use `--configuration-cache` for CI-like builds
- Check cache hit logs to verify optimization
- Clean occasionally: `./gradlew clean cleanBuildCache`
- Update dependencies regularly

### ❌ Don't

- Run Gradle from subdirectories (docs, src, etc.)
- Disable caching unless debugging
- Commit `.gradle/` or `build/` directories
- Run instrumented tests on every PR (use `[test-instrumented]` flag)

---

## More Information

- [Full CI Workflow Guide](./ci-workflow-guide.md) - Complete workflow documentation
- [CI Build Optimization](./ci-build-optimization.md) - Detailed optimization breakdown
- [Gradle Performance](https://docs.gradle.org/current/userguide/performance.html) - Official docs
- [Configuration Cache](https://docs.gradle.org/current/userguide/configuration_cache.html) - Deep
  dive

---

## Quick Reference

### CI Job Status

Check your PR's CI status:

1. Go to your PR on GitHub
2. Scroll to "Checks" section at bottom
3. See which jobs passed/failed
4. Click "Details" to view logs

### Local Build Commands

```bash
# Match CI lint job
./gradlew lintDebug --build-cache --parallel --configure-on-demand --configuration-cache

# Match CI build job
./gradlew assembleDebug bundleDebug --build-cache --parallel --configure-on-demand --configuration-cache

# Match CI test job
./gradlew testDebugUnitTest --build-cache --parallel --configure-on-demand --configuration-cache

# Full CI simulation (without instrumented tests)
./gradlew clean lintDebug assembleDebug bundleDebug testDebugUnitTest \
  --build-cache --parallel --configure-on-demand --configuration-cache
```

---