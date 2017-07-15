package com.sf.util

import com.sf.model.ProductInformation
import java.io.File

/**
 * Created by shenfan on 2017/7/15.
 */
object Helper {
    fun handleConfict(path: String): String {
        val f = File(path)
        if (f.exists()) {
            val count = f.parentFile.listFiles { x -> x.name.equals(f.name, true) }.count()
            return "$path-${count + 1}"
        } else {
            return path
        }
    }

}