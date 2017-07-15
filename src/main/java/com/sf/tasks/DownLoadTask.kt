package com.sf.tasks

import com.sf.FileAccessKT
import com.sf.NetWorkAccessKT
import com.sf.extensions.FileExtension
import com.sf.model.ProductInformation
import com.sf.util.Helper
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by shenfan on 2017/7/15.
 */
class DownLoadTask(val downLoadQueue: ConcurrentLinkedQueue<ProductInformation>) : Runnable {

    //线程中断标记位
    @Volatile var threadStopFlag = false

    override fun run() {
        //删除存在的文件
        val path = File(FileAccessKT.pathPrefix)
        if (path.deleteRecursively()) {
            while (!threadStopFlag) {
                try {
                    //消费下载队列里的数据
                    consumeQueue()
                } catch (e: InterruptedException) {
                    threadStopFlag = true
                }
            }
        }
    }


    fun consumeQueue() {
        synchronized(downLoadQueue) {
            if (downLoadQueue.isNotEmpty()) {
                val productionInfo = downLoadQueue.poll()
                var path = FileExtension.generatePathString(FileAccessKT.pathPrefix, productionInfo.ProductName?.replace(" ", "") ?: "")
                val textWriteToFile = productionInfo.getInfoString().replace("\n", "\r\n")
                //解决路径冲突
                path = Helper.handleConfict(path)
                FileAccessKT.writeTextToFile(textWriteToFile, path, "商品详情.txt")
                val buffer = NetWorkAccessKT.getStreamFromUrl(productionInfo.ImageUrl!!)
                FileAccessKT.writeBufferToFile(buffer, path)
            }
        }
    }
}