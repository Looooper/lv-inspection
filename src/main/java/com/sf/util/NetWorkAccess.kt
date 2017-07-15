package com.sf.util

import java.net.URL

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object NetWorkAccess {
    fun getStreamFromUrl(url: String): ByteArray? {
        try {
            val resource = URL(url.replace(" ", "%20"))
            return resource.readBytes()
        } catch (e: Exception) {
            return null
        }
    }
}
