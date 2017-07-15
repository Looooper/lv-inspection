package com.sf.forms

import com.sf.FileAccessKT
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import javax.swing.*

object StartUp {
    val mainFormKT = MainFormKT()
    //线程中断标记位
    @Volatile var threadStopFlag = false


    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {
        //填充界面文本线程
        val appendTextThread = Thread(Runnable {
            val area: JTextArea = mainFormKT.jTextArea
            mainFormKT.start(area)
        })
        //下载线程
        val downLoadThread = Thread(Runnable {
            //删除存在的文件
            val path = File(FileAccessKT.pathPrefix)
            if (path.deleteRecursively()) {
                while (!threadStopFlag) {
                    try {
                        //消费下载队列里的数据
                        mainFormKT.consumeQueue()
                    } catch (e: InterruptedException) {
                        threadStopFlag = true
                    }
                }
            }
        })

        mainFormKT.jFrame.title = "zz"
        mainFormKT.rootPane.layout = null

        val button = JButton("Fetch")
        button.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                appendTextThread.start()
                downLoadThread.start()
            }
        })
        button.setSize(80, 40)
        button.setLocation(20, 20)
        mainFormKT.rootPane.add(button)

        mainFormKT.jTextArea.setSize(800, 500)
        mainFormKT.jTextArea.setLocation(0, 0)
        mainFormKT.jTextArea.background = Color(255, 255, 255)
        mainFormKT.jTextArea.lineWrap = true
        mainFormKT.jTextArea.font = Font("宋体", Font.PLAIN, 18)

        val scrollPane = JScrollPane(mainFormKT.jTextArea)
        scrollPane.setSize(950, 500)
        scrollPane.setLocation(20, 80)

        mainFormKT.rootPane.add(scrollPane)


        val btnStop = JButton("Stop")
        btnStop.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                try {
                    appendTextThread.stop()
                    downLoadThread.stop()
                    JOptionPane.showMessageDialog(mainFormKT.rootPane, "中断成功", "提示", JOptionPane.YES_OPTION)
                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(mainFormKT.rootPane, "中断失败${e.message}", "提示", JOptionPane.YES_OPTION)
                }
            }
        })
        btnStop.setSize(80, 40)
        btnStop.setLocation(120, 20)
        mainFormKT.rootPane.add(btnStop)

        val btnReset = JButton("Clean")
        btnReset.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                mainFormKT.jTextArea.text = ""
                val path = File(FileAccessKT.pathPrefix)
                if (path.deleteRecursively())
                    JOptionPane.showMessageDialog(mainFormKT.rootPane, "清理成功", "提示", JOptionPane.YES_OPTION)
                else
                    JOptionPane.showMessageDialog(mainFormKT.rootPane, "提示消息", "提示", JOptionPane.WARNING_MESSAGE)

            }
        })
        btnReset.setSize(80, 40)
        btnReset.setLocation(220, 20)
        mainFormKT.rootPane.add(btnReset)

        mainFormKT.jFrame.contentPane = mainFormKT.rootPane
        mainFormKT.jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        mainFormKT.jFrame.pack()
        mainFormKT.jFrame.setSize(1000, 620)
        mainFormKT.jFrame.setLocationRelativeTo(scrollPane)//居中
        mainFormKT.jFrame.isVisible = true
    }
}