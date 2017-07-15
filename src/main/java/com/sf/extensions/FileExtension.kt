package com.sf.extensions

import java.io.File

/**
 * Created by shenfan on 2017/7/15.
 */
object FileExtension {
    fun generatePathString(vararg paths: String): String {
        return paths.reduce({ x, y ->
            if (!x.isNullOrBlank())
                x + File.separator + y
            else
                x + y
        })
    }
}