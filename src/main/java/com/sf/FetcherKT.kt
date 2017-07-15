package com.sf


import com.sf.model.ProductInformation
import org.jsoup.Jsoup
import java.io.IOException

object FetcherKT {
    //获取商品介绍页的数据
    @Throws(IOException::class)
    fun getDataFromProductDetailPage(url: String): ProductInformation {
        val connect = Jsoup.connect(url)
        val document = connect.get()
        //详细特征
        val elemFeatures = document.select(".functional-text").select(".innerContent").first()
        //简介
        val elemIntroduction = document.select(".productDescription").first()
        //价格priceValue price-sheet
        val elemPrice = document.select(".priceValue").first()
        //特性数组
        val features = elemFeatures.text().split("-")

        val productInformation = ProductInformation()
        productInformation.Introduction = elemIntroduction?.text()
        productInformation.Features = features
        productInformation.Price = elemPrice?.text()
        return productInformation
    }
}
