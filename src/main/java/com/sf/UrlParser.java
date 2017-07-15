package com.sf;

/**
 * Created by Shenfan on 2017/7/12.
 */
public class UrlParser {
    static String parseId(String url) {
        int productsWordIndex = url.indexOf("products");
        return url.substring(productsWordIndex + 9);
    }
}
