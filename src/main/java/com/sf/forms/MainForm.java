package com.sf.forms;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import static com.sf.Fetcher.getDataFromProductDetailPage;

/**
 * Created by Shenfan on 2017/7/12.
 */
public class MainForm {
    private static JTextArea jTextArea;

    private static void start(JTextArea area) {
        getMainInfo(area);
    }

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("zz");
        //JPanel rootPane=new MainFrame().mainPanel;
        JPanel rootPane = new JPanel();
        rootPane.setLayout(null);

        JButton button = new JButton("Fetch");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Thread t = new Thread(new Runnable() {
                    final JTextArea area = jTextArea;

                    public void run() {
                        start(area);
                    }
                });
                t.start();
            }
        });
        button.setSize(100, 50);
        button.setLocation(20, 20);
        rootPane.add(button);

        jTextArea = new JTextArea();
        jTextArea.setSize(800, 600);
        jTextArea.setLocation(0, 0);
        jTextArea.setBackground(new Color(255, 255, 255));
        jTextArea.setLineWrap(true);
        jTextArea.setFont(new Font("宋体", Font.PLAIN, 20));

        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setSize(800, 600);
        scrollPane.setLocation(20, 100);
        rootPane.add(scrollPane);

        JButton reset = new JButton("Clear");
        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTextArea.setText("");
            }
        });
        reset.setSize(100, 50);
        reset.setLocation(180, 20);
        rootPane.add(reset);

        jFrame.setContentPane(rootPane);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //jFrame.pack();
        jFrame.setSize(1000, 800);
        //jFrame.setLocationRelativeTo(rootPane);//居中
        jFrame.setVisible(true);
    }

    private static void getMainInfo(JTextArea jTextArea) {
        Connection connect = Jsoup.connect("http://www.louisvuitton.cn/zhs-cn/homepage");
        boolean hasCookies = false;
        try {
            Document document = connect.get();
            Map<String, String> cookies = null;
            //所有三级菜单下的链接
            Elements elements = document.select(".level3").select(".mm-push-link");
            for (Element element : elements) {
                String url = element.attr("href");
                if (url.contains("women") || url.contains("men")) {
                    if (!hasCookies) {
                        cookies = Jsoup.connect(url).execute().cookies();
                        hasCookies = true;
                    }
                    Connection.Response child = Jsoup.connect(url).cookies(cookies).ignoreHttpErrors(true).execute();
                    Document doc = Jsoup.parse(child.body());
                    //列表界面
                    for (Element e : doc.select(".product-item")) {
                        Element desp = e.select(".description").first();
                        Element imgUrl = e.select(".imageWrapper").select("img").first();
                        //商品名
                        if (desp != null) {
                            jTextArea.append(String.format("----------商品名称------------\n%s\n", desp.text()));
                        }
                        if (imgUrl != null) {
                            String url2 = imgUrl.attr("data-src").replace("{IMG_HEIGHT}", "500").replace("{IMG_WIDTH}", "500");
                            jTextArea.append(String.format("----------图片网址------------\n%s\n", url2));
                        }
                        String prefix = "http://www.louisvuitton.cn";
                        jTextArea.append(getDataFromProductDetailPage(prefix + e.attr("href")));
                        int length = jTextArea.getText().length();
                        jTextArea.setCaretPosition(length);
                        //jTextArea.paintImmediately(jTextArea.getBounds());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
