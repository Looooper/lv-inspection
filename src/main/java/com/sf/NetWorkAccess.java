package com.sf;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by Shenfan on 2017/7/12.
 */
public class NetWorkAccess {
    static void DownLoadImage(String urlString) throws IOException {
        String folderName = UrlParser.parseId(urlString);

        // 构造URL
        URL url;
        InputStream is = null;
        // 打开连接
        URLConnection con = null;
        OutputStream os = null;
        try {
            url = new URL(urlString);
            con = url.openConnection();

            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            is = con.getInputStream();


            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(FileAccess.getAbsPath() + folderName);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            os = new FileOutputStream(sf.getPath() + "\\" + UUID.randomUUID().toString());
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }

    }
}
