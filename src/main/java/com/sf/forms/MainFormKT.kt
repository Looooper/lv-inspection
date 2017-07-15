package com.sf.forms

import com.sf.FetcherKT.getDataFromProductDetailPage
import com.sf.FileAccessKT
import com.sf.NetWorkAccessKT
import com.sf.extensions.FileExtension
import com.sf.model.ProductInformation
import org.jsoup.Jsoup
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea

class MainFormKT {
    val prefix = "http://www.louisvuitton.cn"
    val jFrame: JFrame = JFrame()
    val rootPane: JPanel = JPanel()
    val jTextArea: JTextArea = JTextArea()

    fun start(area: JTextArea) {
        getMainInfo(area)
    }

    //下载队列
    val downLoadQueue = ConcurrentLinkedQueue<ProductInformation>()

    /*
    * 获取网页内容，填充文本框
    * jTextArea表示Java窗体中的文本框
    * */
    fun getMainInfo(jTextArea: JTextArea) {
        //连接到LV官网
        val connect = Jsoup.connect("http://www.louisvuitton.cn/zhs-cn/homepage")
        try {
            val document = connect.get() // 获取HTML document主体
            //所有三级菜单下的a标签
            val aLinkElements = document.select(".level3").select(".mm-push-link")
            for (element in aLinkElements) {
                //取a标签的href属性
                val url = element.attr("href")
                //只对男士女士连接做处理，其他连接可能是活动展示类的。用处不大
                if (url.contains("women") || url.contains("men")) {
                    //商品详情页面
                    val productDetailPage = Jsoup.connect(url).ignoreHttpErrors(true).execute()
                    val doc = Jsoup.parse(productDetailPage.body())
                    //列表界面
                    for (node in doc.select(".product-item")) {
                        //商品名称
                        val productName = node.select(".description").first()
                        //图片标签
                        val imageNode = node.select(".imageWrapper").select("img").first()
                        //构建商品信息对象（便于写入文件，下载图片）
                        val productInformation = getDataFromProductDetailPage(prefix + node.attr("href"))
                        productInformation.ProductName = productName.text()
                        //取图片标签的data-src属性得到图片地址
                        productInformation.ImageUrl = imageNode.attr("data-src").replace("{IMG_HEIGHT}", "500").replace("{IMG_WIDTH}", "500")
                        //显示到程序界面文本框
                        jTextArea.append(generateFileInfoString(productInformation))
                        //文本框立即滚动到底部
                        jTextArea.caretPosition = jTextArea.text.length

                        //添加商品到下载队列
                        downLoadQueue.add(productInformation)
                    }
                }
            }
        } catch (e: IOException) {
        }
    }

    fun generateFileInfoString(productInformation: ProductInformation): String {
        val sb = StringBuilder()
        //商品名
        sb.append(String.format("----------商品名称------------\n%s\n", productInformation.ProductName ?: "商品名字缺失"))
        sb.append(String.format("----------图片网址------------\n%s\n", productInformation.ImageUrl ?: "图片地址缺失"))
        sb.append(String.format("------------简介----------------\n%s\n", productInformation.Introduction ?: "商品简介缺失"))
        sb.append("------------特性----------------\n")
        productInformation.Features?.forEach {
            if (!it.isNullOrBlank())
                sb.append("$it\n")
        }
        sb.append(String.format("------------价格----------------\n%s\n", productInformation.Price ?: "商品价格缺失"))
        sb.append("******************************************************************\n")
        if (sb.isNotEmpty())
            return sb.toString()
        else
            return ""
    }

    fun consumeQueue() {
        synchronized(downLoadQueue) {
            if (downLoadQueue.isNotEmpty()) {
                val productionInfo = downLoadQueue.poll()
                var path = FileExtension.generatePathString(FileAccessKT.pathPrefix, productionInfo.ProductName?.replace(" ", "") ?: "")
                val textWriteToFile = generateFileInfoString(productionInfo).replace("\n", "\r\n")
                //解决路径冲突
                path = handleConfict(path)
                FileAccessKT.writeTextToFile(textWriteToFile, path, "商品详情.txt")
                val buffer = NetWorkAccessKT.getStreamFromUrl(productionInfo.ImageUrl!!)
                FileAccessKT.writeBufferToFile(buffer, path)
            }
        }
    }

    fun handleConfict(path: String): String {
        val f = File(path)
        if (f.exists()) {
            val count = f.listFiles().count()
            return "$path-${count + 1}"
        } else {
            return path
        }
    }
}
