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

package com.droidconke.chailinter.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiMember
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.jetbrains.uast.*

/**
 * This checks whether there are incorrect usages of Compose Material APIs over equivalents in
 * the chai design system. Ideally its to be as dictatorial as possible to prevent people from using 
 * default material designs and moving away from the design system.
 * 
 * We have tried to capture as much as many UI scenarios as possible in chai. Incase of a missing
 * design component, then it is recommended to submit an issue and then implement the
 * component missing in chai.
 */

class ChaiIncorrectUsageDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(
            UCallExpression::class.java,
            UQualifiedReferenceExpression::class.java,
            USimpleNameReferenceExpression::class.java
        )
    }
    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {

            override fun visitCallExpression(node: UCallExpression) {
                val name = node.methodName ?: return
                val wrapperName = node.resolve()?.containingClass?.qualifiedName ?: return

            }

            override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
                val aFunction = (node.receiver.getExpressionType() as? PsiClassReferenceType)
                    ?.reference?.qualifiedName
                    ?: return

            }

            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
                val className = (node.resolve() as? PsiMember)?.containingClass?.qualifiedName ?: return
            }
            
        }
    }
    /*Adding this as an empty array but will be replaced later when adding implementations*/
    companion object {
        val ISSUE: Any = EMPTY_ARRAY
    }
}

/**
 *  COMPONENTS_NAMES are various ways a developer may deviate away from the use of
 *  chai Design system elements and components and use material or material 3 components:
 *
 *  "androidx.compose.material.*" or "androidx.compose.material3.*"
 *
 *  instead of using the design system stated in chai design system.
 *
 *  We implement an approach for contributors to stick to designs already agreed when implementing the
 *  UI and proposing changes
 *
 *  private val METHOD_NAMES = mapOf(
 *   "com.droidconke.chai.components.foo" to setOf(
 *      "androidx.compose.material.FooBar",
 *      "androidx.compose.material3.FooBar",
 *      "androidx.compose.material3.bar2",
 *      ),
 *      )
 *   Meaning of above lines of code:
 *
 * ["com.droidconke.chai.components.foo" to setOf(]
 *  - This is a design system implementation of a design element that may exist in the
 *
 * below are possible violations that can be added from compose by not choosing to use the above
 * design system implementation and choosing to use material instead of the design system:
 *      "androidx.compose.material.FooBar",
 *      "androidx.compose.material3.FooBar",
 *      "androidx.compose.material3.bar2",
 *
 * NOTE:
 *  In its final state the MethodNames ["com.droidconke.chai.components.foo"] will need to be
 *  mapped to specific instances where a contributor may violate the UI implementations of the
 *  Design system by adding them as:
 *      ["androidx.compose.material.FooBar",]
 *      ["androidx.compose.material3.FooBar",]
 *      ["androidx.compose.material3.bar2",]
 *
 */
//To be implemented with above format
private val METHOD_NAMES = mapOf<String, Int>()