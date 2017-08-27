package com.sf.util

import java.net.URL

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object NetWorkAccess {
    fun getStreamFromUrl(url: String): ByteArray? {
        return try {
            val resource = URL(url.replace(" ", "%20"))
            resource.readBytes()
        } catch (e: Exception) {
            null
        }
    }
}
