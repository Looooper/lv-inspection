package com.sf.util

import java.io.File

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object Helper {

    fun handleConflict(path: String): String {
        val f = File(path)
        return if (f.exists()) {
            val count = f.parentFile.listFiles { x -> x.name.equals(f.name, true) }.count()
            "$path-${count + 1}"
        } else {
            path
        }
    }

}