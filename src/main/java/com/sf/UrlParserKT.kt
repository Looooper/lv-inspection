package com.sf

/**
 * Created by Shenfan on 2017/7/12.
 */
object UrlParserKT {
    internal fun parseId(url: String): String {
        val productsWordIndex = url.indexOf("products")
        return url.substring(productsWordIndex + 9)
    }
}
