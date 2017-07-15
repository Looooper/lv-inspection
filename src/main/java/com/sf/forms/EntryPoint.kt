package com.sf.forms

import com.sf.FileAccess
import com.sf.FileAccessKT
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.WindowConstants


val threadStopFlag = false

@Throws(IOException::class)
fun main(args: Array<String>) {
    val mainFormKT = MainFormKT()
    mainFormKT.jFrame.title = "zz"
    mainFormKT.rootPane.layout = null

    val button = JButton("Fetch")
    button.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            val t = Thread(Runnable {
                val area: JTextArea = mainFormKT.jTextArea
                mainFormKT.start(area)
            })
            t.start()

            val downLoadThread = Thread(Runnable {
                //删除存在的文件
                val path = File(FileAccessKT.pathPrefix)
                if (path.deleteRecursively()) {
                    while (true) {
                        mainFormKT.consumeQueue()
                    }
                }
            })
            downLoadThread.start()
        }
    })
    button.setSize(100, 50)
    button.setLocation(20, 20)
    mainFormKT.rootPane.add(button)

    mainFormKT.jTextArea.setSize(800, 600)
    mainFormKT.jTextArea.setLocation(0, 0)
    mainFormKT.jTextArea.background = Color(255, 255, 255)
    mainFormKT.jTextArea.lineWrap = true
    mainFormKT.jTextArea.font = Font("宋体", Font.PLAIN, 20)

    val scrollPane = JScrollPane(mainFormKT.jTextArea)
    scrollPane.setSize(950, 600)
    scrollPane.setLocation(20, 100)

    mainFormKT.rootPane.add(scrollPane)


    val btnStop = JButton("Stop")
    btnStop.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {

        }
    })
    btnStop.setSize(100, 50)
    btnStop.setLocation(120, 20)
    mainFormKT.rootPane.add(btnStop)

    val btnReset = JButton("Clean")
    btnReset.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            mainFormKT.jTextArea.text = ""
            val path = File(FileAccessKT.pathPrefix)
            path.deleteRecursively()
        }
    })
    btnReset.setSize(100, 50)
    btnReset.setLocation(220, 20)
    mainFormKT.rootPane.add(btnReset)

    mainFormKT.jFrame.contentPane = mainFormKT.rootPane
    mainFormKT.jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    mainFormKT.jFrame.pack()
    mainFormKT.jFrame.setSize(1000, 800)
    mainFormKT.jFrame.setLocationRelativeTo(scrollPane)//居中
    mainFormKT.jFrame.isVisible = true
}

