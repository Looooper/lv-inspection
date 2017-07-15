package com.sf.util

import java.io.File

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object FileUtil {
    fun generatePathString(vararg paths: String): String {
        return paths.reduce({ x, y ->
            if (!x.isNullOrBlank())
                x + File.separator + y
            else
                x + y
        })
    }
}