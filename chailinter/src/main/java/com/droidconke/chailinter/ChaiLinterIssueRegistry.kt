/**
 * Copyright 2023 droidconke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidconke.chailinter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.droidconke.chailinter.detector.ChaiIncorrectUsageDetector
import com.droidconke.chailinter.detector.IncorrectColourUsageDetector

/**
 *
 * An issue is a potential bug in in the app.
 * To discover an issue we use [detectors] that have associated severities - [Severity].
 *
 * [Issues] and [detectors] are separate classes because a detector can discover multiple different issues
 * as it's analyzing code, and we want to be able to different severities for different issues,
 * the ability to suppress one but not other issues from the same detector, and so on.
 *
 */
class ChaiLinterIssueRegistry : IssueRegistry() {

    /**
     * This Lists the various detectors that have been created to detect incorrect Chai Design
     * system usage based on the chai design system. They will exist in the [detectors] package.
     *
     */
    @Suppress("UNCHECKED_CAST")
    override val issues: List<Issue> = listOf(
        ChaiIncorrectUsageDetector.ISSUE,
        IncorrectColourUsageDetector.ISSUE,
    ) as List<Issue>

    override val api: Int = CURRENT_API
    override val minApi: Int = 12

    override val vendor = Vendor(
        vendorName = "Chai",
        identifier = "Chai: lint issue",
        feedbackUrl = "https://github.com/droidconKE/chai/issues/new/choose",
        contact = "https://github.com/droidconKE/chai"
    )
}