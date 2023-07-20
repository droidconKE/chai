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
            USimpleNameReferenceExpression::class.java,
        )
    }
    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {}
    }

    companion object {
        val ISSUE: Any = TODO()
    }
}