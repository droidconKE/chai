package com.droidconke.chailinter.detector

import com.android.tools.lint.detector.api.Detector
import org.jetbrains.uast.EMPTY_ARRAY

/**
 * This checks whether there are incorrect implementations of colours over equivalents in
 * the chai design system. Ideally its to be as dictatorial as possible to prevent people from using
 * default material3 colours and moving away from the design system.
 *
 * In this case, it will throw a warning whenever hardcoded colours are used instead of the ones
 * provided by chai design system.
 *
 * We have tried to capture as much as many UI scenarios as possible in chai. In the case of a missing
 * design component, it is recommended to [submit an issue](https://github.com/droidconKE/chai/issues/new/choose) and then implement the
 * component missing in chai.
 *
 */

class IncorrectColourUsageDetector : Detector(), Detector.UastScanner {


    /*Adding this as an empty array but will be replaced later when adding implementations*/
    companion object {
        val ISSUE: Any = EMPTY_ARRAY
    }
}