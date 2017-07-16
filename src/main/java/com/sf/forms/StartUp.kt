package com.sf.forms

import com.sf.model.ProductInformation
import com.sf.tasks.AppendTextTask
import com.sf.tasks.DownLoadTask
import com.sf.util.FileAccess
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import javax.swing.*

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

object StartUp {
    // 页面控件
    val jFrame = JFrame()
    val rootPane = JPanel()
    val jTextArea = JTextArea()
    val scrollPane = JScrollPane(jTextArea)
    val buttonFetch = JButton("Fetch")
    val buttonStop = JButton("Stop")
    val buttonClean = JButton("Clean")

    // 下载队列
    val downLoadQueue = ConcurrentLinkedQueue<ProductInformation>()
    // 线程池
    val execService = Executors.newCachedThreadPool()!!
    // 是否已经开始
    var fetchStarted = false


    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {
        // 初始化页面控件
        initControl()

        var appendTextTask: AppendTextTask? = null
        var downLoadTask: DownLoadTask? = null

        // 点击Fetch按钮的事件
        buttonFetch.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (fetchStarted) {
                    JOptionPane.showMessageDialog(rootPane, "已经开始了", "提示", JOptionPane.WARNING_MESSAGE)
                    return
                }
                // 填充界面文本线程
                appendTextTask = AppendTextTask(downLoadQueue, jTextArea)
                // 下载线程
                downLoadTask = DownLoadTask(downLoadQueue)
                // 开始执行后台任务
                execService.execute(appendTextTask)
                execService.execute(downLoadTask)
                fetchStarted = true
            }
        })

        // 点击停止按钮的事件
        buttonStop.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                try {
                    fetchStarted = false
                    // 设置标记位  停止后台任务的执行
                    appendTextTask?.threadStopFlag = true
                    downLoadTask?.threadStopFlag = true

                    JOptionPane.showMessageDialog(rootPane, "中断成功", "提示", JOptionPane.YES_OPTION)
                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(rootPane, "中断失败${e.message}", "提示", JOptionPane.YES_OPTION)
                }
            }
        })

        // 点击清除按钮的事件
        buttonClean.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                jTextArea.text = ""
                val path = File(FileAccess.pathPrefix)
                // 存在路径，就递归删除
                if (path.deleteRecursively())
                    JOptionPane.showMessageDialog(rootPane, "清理成功", "提示", JOptionPane.YES_OPTION)
                else
                    JOptionPane.showMessageDialog(rootPane, "提示消息", "提示", JOptionPane.WARNING_MESSAGE)

            }
        })
    }

    fun initControl() {
        jFrame.title = "zz"
        rootPane.layout = null

        buttonFetch.setSize(80, 40)
        buttonFetch.setLocation(20, 20)
        rootPane.add(buttonFetch)

        jTextArea.setSize(800, 500)
        jTextArea.setLocation(0, 0)
        jTextArea.background = Color(255, 255, 255)
        jTextArea.lineWrap = true
        jTextArea.font = Font("宋体", Font.PLAIN, 18)


        scrollPane.setSize(950, 500)
        scrollPane.setLocation(20, 80)
        rootPane.add(scrollPane)

        buttonStop.setSize(80, 40)
        buttonStop.setLocation(120, 20)
        rootPane.add(buttonStop)

        buttonClean.setSize(80, 40)
        buttonClean.setLocation(220, 20)
        rootPane.add(buttonClean)

        jFrame.contentPane = rootPane
        jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        jFrame.pack()
        jFrame.setSize(1000, 620)
        jFrame.setLocationRelativeTo(scrollPane)//居中
        jFrame.isVisible = true
    }
}