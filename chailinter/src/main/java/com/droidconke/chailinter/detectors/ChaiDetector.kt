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

package com.droidconke.chailinter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

/**
 * This detector checks for incorrect usages of Compose Material APIs over those in
 *  Chai design system.
 */

class ChaiDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(
            UCallExpression::class.java,
            UQualifiedReferenceExpression::class.java,
            USimpleNameReferenceExpression::class.java
        )
    }
    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {}
    }

    companion object {
        val ISSUE: Any = TODO()
    }
}