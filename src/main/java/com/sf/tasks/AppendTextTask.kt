package com.sf.tasks

import com.sf.FetcherKT
import com.sf.model.ProductInformation
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.JTextArea

/**
 * Created by shenfan on 2017/7/15.
 */
class AppendTextTask(val downLoadQueue: ConcurrentLinkedQueue<ProductInformation>,
                     val jTextArea: JTextArea) : Runnable {

    //线程中断标记位
    @Volatile var threadStopFlag = false

    val prefix = "http://www.louisvuitton.cn"

    override fun run() {
        appendTextArea(jTextArea)
    }

    /*
     * 获取网页内容，填充文本框
     * jTextArea表示Java窗体中的文本框
     * */
    fun appendTextArea(jTextArea: JTextArea) {
        //连接到LV官网
        val connect = Jsoup.connect("http://www.louisvuitton.cn/zhs-cn/homepage")
        try {
            val document = connect.get() // 获取HTML document主体
            //所有三级菜单下的a标签
            val aLinkElements = document.select(".level3").select(".mm-push-link")
            for (element in aLinkElements) {
                if (!threadStopFlag) {
                    try {
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
                                val productInformation = FetcherKT.getDataFromProductDetailPage(prefix + node.attr("href"))
                                productInformation.ProductName = productName.text()
                                //取图片标签的data-src属性得到图片地址
                                productInformation.ImageUrl = imageNode.attr("data-src").replace("{IMG_HEIGHT}", "500").replace("{IMG_WIDTH}", "500")
                                //显示到程序界面文本框
                                jTextArea.append(productInformation.getInfoString())
                                //文本框立即滚动到底部
                                jTextArea.caretPosition = jTextArea.text.length

                                //添加商品到下载队列
                                downLoadQueue.add(productInformation)
                            }
                        }
                    } catch (e: InterruptedException) {
                        threadStopFlag = true
                    }
                }
            }
        } catch (e: IOException) {
        }
    }
}