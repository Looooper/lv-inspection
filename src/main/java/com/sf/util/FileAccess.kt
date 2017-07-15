package com.sf.util

import java.io.File

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object FileAccess {
    val pathPrefix = FileUtil.generatePathString(System.getProperty("user.dir"), "downloads")

    fun writeBufferToFile(buffer: ByteArray?, path: String, fileName: String = "商品图片.jpg") {
        //写入文件
        val output = File(path, fileName)
        if (output.exists()) {
            output.delete()
        }
        if (buffer != null)
            output.writeBytes(buffer)
    }

    fun writeTextToFile(text: String, path: String, fileName: String) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(path, fileName)
        if (file.exists()) {
            file.delete()
        } else {
            file.createNewFile()
        }
        file.writeText(text)
    }
}
