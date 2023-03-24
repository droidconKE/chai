package com.droidconke.chai.exceptions

import java.io.FileNotFoundException

@Suppress("SwallowedException")
fun <T> attemptAssetRetrieval(block: () -> T) = try {
    block()
} catch (e: FileNotFoundException) {
    // Throw chai exception instead
    throw ChaiException("${e.message} is not located in asset folder")
}
