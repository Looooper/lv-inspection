package com.sf.tasks

import com.sf.model.ProductInformation
import com.sf.util.FileAccess
import com.sf.util.FileUtil
import com.sf.util.Helper
import com.sf.util.NetWorkAccess
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

/**
 * 下载图片的后台任务
 */
class DownLoadTask(val downLoadQueue: ConcurrentLinkedQueue<ProductInformation>) : Runnable {

    // 线程中断标记位
    @Volatile var threadStopFlag = false

    override fun run() {
        // 删除存在的文件
        val path = File(FileAccess.pathPrefix)
        if (path.deleteRecursively()) {
            while (!threadStopFlag) {
                try {
                    // 消费下载队列里的数据
                    consumeQueue()
                } catch (e: InterruptedException) {
                    threadStopFlag = true
                }
            }
        }
    }

    // 消费下载队列
    fun consumeQueue() {
        // 因为连续调用isNotEmpty和poll方法并不是原子操作，所以需要同步
        synchronized(downLoadQueue) {
            if (downLoadQueue.isNotEmpty()) {
                val productionInfo = downLoadQueue.poll()
                var path = FileUtil.generatePathString(FileAccess.pathPrefix, productionInfo.ProductName?.replace(" ", "") ?: "")
                val textWriteToFile = productionInfo.getInfoString().replace("\n", "\r\n")
                // 解决路径冲突
                path = Helper.handleConflict(path)
                FileAccess.writeTextToFile(textWriteToFile, path, "商品详情.txt")
                val buffer = NetWorkAccess.getStreamFromUrl(productionInfo.ImageUrl!!)
                FileAccess.writeBufferToFile(buffer, path)
            }
        }
    }
}