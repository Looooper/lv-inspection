package com.sf.util

import java.io.File

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object FileUtil {
    fun generatePathString(vararg paths: String): String {
        return paths.reduce({ x, y ->
            if (!x.isBlank())
                x + File.separator + y
            else
                x + y
        })
    }
}