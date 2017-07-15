package com.sf;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

public class Fetcher {
    //获取商品介绍页的数据
    public static String getDataFromProductDetailPage(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        Connection connect = Jsoup.connect(url);

        Document document = connect.get();
        //详细特征
        Element elemFeatures = document.select(".functional-text").select(".innerContent").first();
        //简介
        Element elemIntroduction = document.select(".productDescription").first();
        //价格priceValue price-sheet
        Element elemPrice = document.select(".priceValue").first();

        String[] features = elemFeatures.text().split("-");
        if (elemIntroduction != null) {
            sb.append(String.format("------------简介----------------\n%s\n", elemIntroduction.text()));
        }

        sb.append(String.format("------------特性----------------\n%s\n", Arrays.toString(features)));

        if (elemPrice != null) {
            sb.append(String.format("------------价格----------------\n%s\n", elemPrice.text()));
        }

        sb.append("******************************************************************\n");

        int count = sb.length();
        if (count > 0) {
            return sb.toString();
        }
        return null;

    }
}
