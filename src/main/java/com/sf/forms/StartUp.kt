package com.sf.forms

import com.sf.FileAccessKT
import com.sf.model.ProductInformation
import com.sf.tasks.AppendTextTask
import com.sf.tasks.DownLoadTask
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.*

object StartUp {
    val jFrame = JFrame()
    val rootPane = JPanel()
    val jTextArea = JTextArea()

    //下载队列
    val downLoadQueue = ConcurrentLinkedQueue<ProductInformation>()
    //是否已经开始
    var fetchStarted = false

    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {

        var run1: AppendTextTask? = null
        var run2: DownLoadTask? = null
        jFrame.title = "zz"
        rootPane.layout = null

        val button = JButton("Fetch")
        button.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (fetchStarted) {
                    JOptionPane.showMessageDialog(rootPane, "已经开始了", "提示", JOptionPane.WARNING_MESSAGE)
                    return
                }
                //填充界面文本线程
                run1 = AppendTextTask(downLoadQueue, jTextArea)
                val appendTextThread = Thread(run1)
                //下载线程
                run2 = DownLoadTask(downLoadQueue)
                val downLoadThread = Thread(run2)

                appendTextThread.start()
                downLoadThread.start()
                fetchStarted = true
            }
        })
        button.setSize(80, 40)
        button.setLocation(20, 20)
        rootPane.add(button)

        jTextArea.setSize(800, 500)
        jTextArea.setLocation(0, 0)
        jTextArea.background = Color(255, 255, 255)
        jTextArea.lineWrap = true
        jTextArea.font = Font("宋体", Font.PLAIN, 18)

        val scrollPane = JScrollPane(jTextArea)
        scrollPane.setSize(950, 500)
        scrollPane.setLocation(20, 80)

        rootPane.add(scrollPane)


        val btnStop = JButton("Stop")
        btnStop.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                try {
                    fetchStarted = false
                    run1?.threadStopFlag = true
                    run2?.threadStopFlag = true
                    JOptionPane.showMessageDialog(rootPane, "中断成功", "提示", JOptionPane.YES_OPTION)
                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(rootPane, "中断失败${e.message}", "提示", JOptionPane.YES_OPTION)
                }
            }
        })
        btnStop.setSize(80, 40)
        btnStop.setLocation(120, 20)
        rootPane.add(btnStop)

        val btnReset = JButton("Clean")
        btnReset.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                jTextArea.text = ""
                val path = File(FileAccessKT.pathPrefix)
                if (path.deleteRecursively())
                    JOptionPane.showMessageDialog(rootPane, "清理成功", "提示", JOptionPane.YES_OPTION)
                else
                    JOptionPane.showMessageDialog(rootPane, "提示消息", "提示", JOptionPane.WARNING_MESSAGE)

            }
        })
        btnReset.setSize(80, 40)
        btnReset.setLocation(220, 20)
        rootPane.add(btnReset)

        jFrame.contentPane = rootPane
        jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        jFrame.pack()
        jFrame.setSize(1000, 620)
        jFrame.setLocationRelativeTo(scrollPane)//居中
        jFrame.isVisible = true
    }
}