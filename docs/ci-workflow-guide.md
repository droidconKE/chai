# CI/CD Workflow Guide

## Overview

This document explains the workflow structure for the Chai Design System GitHub Actions.

## Workflow Structure

### 🔄 Jobs Pipeline

```
cancel-previous → lint → build → test → androidTest (conditional)
```

For running all jobs sequentially with depndencies to ensure code quality before proceeding.

---

## Jobs Breakdown

### 1. **cancel-previous**

- **Purpose**: Cancels duplicate/outdated workflow runs
- **When**: Always runs on every push/PR
- **Why**: Saves CI minutes by stopping old builds when new commits are pushed

### 2. **lint**

- **Purpose**: Static code analysis (Android Lint)
- **When**: Always runs after cancel-previous
- **Runs**: `./gradlew lintDebug`
- **Artifacts**: Lint reports (HTML & XML)
- **What it checks**:
    - Code quality issues
    - Android best practices violations
    - Potential bugs
    - Security vulnerabilities

### 3. **build**

- **Purpose**: Compile and build the project
- **When**: Always runs after lint passes
- **Runs**:
    - `./gradlew assembleDebug` - Builds APK
    - `./gradlew bundleDebug` - Builds AAB (Android App Bundle)
- **Artifacts**:
    - Debug APK (14 days retention)
    - Debug Bundle (14 days retention)

### 4. **test**

- **Purpose**: Run unit tests
- **When**: Always runs after build succeeds
- **Runs**: `./gradlew testDebugUnitTest`
- **Tests**: Fast, JVM-based unit tests (no emulator needed)
- **Artifacts**: Test reports and results (7 days retention)

### 5. **androidTest** (Conditional)

- **Purpose**: Run instrumented tests on Android emulator
- **When**: Only runs if:
    - ✅ Pushing to `main` or `develop` branches
    - ✅ Commit message contains `[test-instrumented]`
    - ❌ Skipped on regular PRs (saves ~10-15 minutes)
- **Runs**: `./gradlew connectedDebugAndroidTest`
- **Tests**: UI tests, integration tests requiring Android framework
- **API Level**: 34 (Android 14)
- **Artifacts**: Instrumented test reports (7 days retention)

---

## Test Types Explained

### **Unit Tests** (test job)

- **Location**: `src/test/`
- **Run on**: JVM (fast, no emulator)
- **Purpose**: Test busines logic, utilities, view models
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

### **E2E Tests** (not implemened)

- **Purpose**: Test complete user flows across the entire app
- **Example**: User opens app → navigates through all screens → interacts with all components
- **Note**: ! Not typicly needed for a component library, bt will add it for a flow:)

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

All Gradle commands use:

- `--build-cache` - Reuses outputs from previous builds
- `--parallel` - Runs independent tasks in parallel
- `--configure-on-demand` - Only configures needed projects

### 🗂️ Caching Strategy

1. **Gradle dependencies**: Cached by `gradle/actions/setup-gradle@v4`
2. **Android Emulator (AVD)**: Cached to avoid ~3-5 min setup time
3. **Cache policy**: Read-only for PRs, read-write for main/develop

### 💰 Cost Savings

- **Unit tests**: ~2-3 minutes (free on GitHub-hosted runners)
- **Instrumented tests**: ~10-15 minutes (expensive, hence conditional)
- **Estimated monthly savings**: ~$50-100 by making androidTest conditional

---

## Recommendations

### For Design System Development:

1. **Unit tests**: Write for utilities, theme logic, non-UI code
2. **Instrumented tests**: Write for:
    - Component rendering validation
    - Theme switching behavior
    - Accessibility semantics
    - Screenshot/visual regression tests
3. **E2E tests**: Not needed for a component library

### When to Run Full Suite:

- ✅ Before releases
- ✅ After major refactoring
- ✅ When adding new components
- ❌ On every small PR (wastes time/money)

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

- Consider removing androidTest job if not needed
- Reduce matrix API levels (currently only API 34)
- Optimize Gradle build with more aggressive caching
