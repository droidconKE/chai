# CI/CD Workflow Guide

## Overview

The Chai Design System uses GitHub Actions for continuous integration and deployment. This document
explains the workflow structure and when each job runs.

## Workflow Structure

### 🔄 Jobs Pipeline (Parallel Execution)

```
cancel-previous (15s)
    ↓
    ├─ lint (1m 20s)
    ├─ build (1m 40s)  } Running in PARALLEL
    └─ test (50s)
    ↓
androidTest (8-9m - conditional, only after all pass)
```

**Total time: ~2m 25s** (without androidTest) - Jobs run in parallel for maximum speed!

For detailed optimization information,
see [CI Build Optimization Guide](./ci-build-optimization.md).

---

## Jobs Breakdown

### 1. **cancel-previous**

- **Purpose**: Cancels duplicate/outdated workflow runs
- **When**: Always runs on every push/PR
- **Why**: Saves CI minutes by stopping old builds when new commits are pushed

### 2. **lint** (Runs in parallel with build & test)

- **Purpose**: Static code analysis (Android Lint)
- **When**: Runs in parallel after cancel-previous
- **Runs**:
  `./gradlew lintDebug --build-cache --parallel --configure-on-demand --configuration-cache`
- **Artifacts**: Lint reports (HTML & XML)
- **What it checks**:
    - Code quality issues
    - Android best practices violations
    - Potential bugs
    - Security vulnerabilities
- **Timing**: ~1m 20s (first run), ~40s (cached)

### 3. **build** (Runs in parallel with lint & test)

- **Purpose**: Compile and build the project
- **When**: Runs in parallel after cancel-previous
- **Runs**:
    -
    `./gradlew assembleDebug --build-cache --parallel --configure-on-demand --configuration-cache` -
    Builds APK
    - `./gradlew bundleDebug --build-cache --parallel --configure-on-demand --configuration-cache` -
      Builds AAB (Android App Bundle)
- **Artifacts**:
    - Debug APK (14 days retention)
    - Debug Bundle (14 days retention)
- **Timing**: ~1m 40s (first run), ~50s (cached)

### 4. **test** (Runs in parallel with lint & build)

- **Purpose**: Run unit tests
- **When**: Runs in parallel after cancel-previous
- **Runs**:
  `./gradlew testDebugUnitTest --build-cache --parallel --configure-on-demand --configuration-cache`
- **Tests**: Fast, JVM-based unit tests (no emulator needed)
- **Artifacts**: Test reports and results (7 days retention)
- **Timing**: ~50s (first run), ~30s (cached)

### 5. **androidTest** (Conditional - runs after all parallel jobs pass)

- **Purpose**: Run instrumented tests on Android emulator
- **When**: Only runs if:
    - ✅ Pushing to `main` or `develop` branches
    - ✅ Commit message contains `[test-instrumented]`
    - ❌ Skipped on regular PRs (saves ~10-15 minutes)
- **Runs**:
  `./gradlew connectedDebugAndroidTest --build-cache --parallel --configure-on-demand --configuration-cache`
- **Tests**: UI tests, integration tests requiring Android framework
- **API Level**: 34 (Android 14)
- **Artifacts**: Instrumented test reports (7 days retention)
- **Timing**: ~8-9m (with AVD cache), ~10-12m (first run)

---

## Test Types Explained

### **Unit Tests** (test job)

- **Location**: `src/test/`
- **Run on**: JVM (fast, no emulator)
- **Purpose**: Test business logic, utilities, view models
- **Example**: Testing color utility functions, theme logic

```kotlin
@Test
fun `test color alpha calculation`() {
    val color = Color.Red
    val result = color.withAlpha(0.5f)
    assertEquals(0.5f, result.alpha, 0.01f)
}
```

### **Instrumented Tests** (androidTest job)

- **Location**: `src/androidTest/`
- **Run on**: Android emulator or real device
- **Purpose**: Test UI components, Android APIs, compose rendering
- **Example**: Testing button click interactions

```kotlin
@Test
fun `CPrimaryButton displays text and handles clicks`() {
    composeTestRule.setContent {
        ChaiTheme {
            CPrimaryButton(onClick = { /* ... */ }) {
                Text("Click me")
            }
        }
    }
    composeTestRule.onNodeWithText("Click me").assertExists()
    composeTestRule.onNodeWithText("Click me").performClick()
}
```

### **E2E Tests** (not implemented)

- **Purpose**: Test complete user flows across the entire app
- **Example**: User opens app → navigates through all screens → interacts with all components
- **Note**: Not typically needed for a component library, but will add it for demo app flows

---

## Triggering Instrumented Tests

Since instrumented tests are slow and resource-intensive, they're conditional:

### Option 1: Merge to main/develop

```bash
git push origin main
# androidTest job will run automatically
```

### Option 2: Use commit message flag

```bash
git commit -m "Add new button variants [test-instrumented]"
git push origin feature/new-buttons
# androidTest job will run on this PR
```

### Option 3: Always run (modify workflow)

Remove the `if:` condition from the androidTest job if you want it to run on every PR.

---

## Performance Optimizations

### ⚡ Gradle Flags

All Gradle commands now use **4 performance flags**:

- `--build-cache` - Reuses outputs from previous builds (20-40% faster)
- `--parallel` - Runs independent tasks in parallel (15-30% faster)
- `--configure-on-demand` - Only configures needed projects (10-20% faster)
- `--configuration-cache` - Caches configuration phase (**30-50% faster**)

**Combined impact: 50-70% faster builds on cache hits!**

### 🗂️ Caching Strategy

1. **Gradle dependencies**: Cached by `gradle/actions/setup-gradle@v4`
2. **Configuration cache**: Caches Gradle configuration phase (NEW!)
3. **Build cache**: Reuses task outputs across builds
4. **Android Emulator (AVD)**: Cached to avoid ~3-5 min setup time
5. **Cache policy**: Read-only for PRs, read-write for main/develop

### 🚀 Parallel Execution

**Key Change:** Jobs now run in parallel instead of sequentially!

**Before (Sequential):**

```
cancel → lint (1m30s) → build (2m) → test (1m) = ~4m 45s
```

**After (Parallel):**

```
cancel → [lint (1m20s) + build (1m40s) + test (50s)] = ~2m 25s
```

**Time saved: 48% faster!**

### 💰 Cost Savings

- **Fresh build**: ~2m 25s (down from ~4m 45s)
- **Cached build**: ~50s (down from ~3m 30s) - **76% faster!**
- **Instrumented tests**: ~8-9m (down from ~10-12m)
- **Monthly savings**: Can handle 2x more builds in same time OR save ~500 minutes/month

For detailed breakdown, see [CI Build Optimization Guide](./ci-build-optimization.md).

---

## Build Timing Expectations

### **Fresh Build (No Cache)**

- Lint: ~1m 20s
- Build: ~1m 40s
- Test: ~50s
- **Total: ~2m 25s** (longest job wins since parallel)

### **Cached Build (After First Run)**

- Lint: ~40s
- Build: ~50s
- Test: ~30s
- **Total: ~50s**  **(76% faster!)**

### **Instrumented Tests (Conditional)**

- First run: ~10-12m
- With AVD cache: ~8-9m
- Only runs on main/develop or with `[test-instrumented]`

---

## Recommendations

### For Design System Development:

1. **Unit tests**: Write for utilities, theme logic, non-UI code
2. **Instrumented tests**: Write for:
    - Component rendering validation
    - Theme switching behavior
    - Accessibility semantics
    - Screenshot/visual regression tests
3. **E2E tests**: Optional for demo app user flows

### When to Run Full Suite:

- Before releases
- After major refactoring
- When adding new components
- On every small PR (wastes time/money)

---

## CI Configuration Details

### **Enhanced CI Gradle Properties**

Located in `.github/ci-gradle.properties`:

```properties
org.gradle.workers.max=4                    # 4 parallel workers
kotlin.incremental=true                     # Incremental compilation
org.gradle.configuration-cache=true         # Configuration cache (HUGE boost)
org.gradle.jvmargs=-Xmx4096m               # 4GB heap for faster builds
-XX:+UseParallelGC                         # Better GC for CI
```

See [CI Build Optimization Guide](./ci-build-optimization.md) for full details.

---

## Future Improvements

Consider adding:

- [ ] **Screenshot testing** with Paparazzi or Roborazzi
- [ ] **Visual regression testing** to catch UI changes
- [ ] **Accessibility testing** with Espresso accessibility checks
- [ ] **Performance testing** for compose recomposition
- [ ] **Dependency updates** with Renovate/Dependabot

---

## Troubleshooting

### "Instrumented tests not running on my PR"

- Check if your commit message contains `[test-instrumented]`
- Or merge to main/develop branch
- Or remove the `if:` condition in the workflow

### "Tests failing on emulator"

- Check KVM is enabled (Ubuntu runners only)
- Verify AVD cache is valid
- Try clearing AVD cache by bumping the cache key

### "Workflow is too slow"

First, check if you're getting cache hits:

1. Look for "Configuration cache entry reused" in logs
2. Check for "FROM-CACHE" for build tasks
3. Verify Gradle setup completes in <15s

If still slow:

- See [CI Build Optimization Guide](./ci-build-optimization.md) for troubleshooting
- Consider increasing workers or memory in `ci-gradle.properties`
- Check for configuration cache problems: `./gradlew --configuration-cache help`

### "Configuration cache errors"

Some plugins may not be compatible with configuration cache. If you see errors:

```bash
# Navigate to project root first
cd /path/to/chai  # Make sure you're in the project root, not in /docs

# Test locally
./gradlew clean build --configuration-cache

# Temporarily disable if needed
./gradlew build --no-configuration-cache
```

**Note:** Always run Gradle commands from the **project root directory** (where `gradlew` is
located), not from subdirectories like `docs/`.

See
the [Gradle Configuration Cache docs](https://docs.gradle.org/current/userguide/configuration_cache.html)
for details.

---

## Additional Resources

- [CI Build Optimization Guide](./ci-build-optimization.md) - Detailed optimization breakdown
- [Gradle Performance Guide](https://docs.gradle.org/current/userguide/performance.html)
- [Configuration Cache](https://docs.gradle.org/current/userguide/configuration_cache.html)
- [GitHub Actions Optimization](https://docs.github.com/en/actions/using-workflows/caching-dependencies-to-speed-up-workflows)
- [Android Build Performance](https://developer.android.com/studio/build/optimize-your-build)

