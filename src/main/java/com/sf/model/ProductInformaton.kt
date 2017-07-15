package com.sf.model

/**
 * Created by Shenfan on 2017/7/14.
 */
class ProductInformation {
    var ProductName: String? = ""
    var Introduction: String? = ""
    var Features: List<String>? = listOf<String>()
    var ImageUrl: String? = ""
    var Price: String? = ""

    fun getInfoString(): String {
        val sb = StringBuilder()
        //商品名
        sb.append(String.format("----------商品名称------------\n%s\n", this.ProductName ?: "商品名字缺失"))
        sb.append(String.format("----------图片网址------------\n%s\n", this.ImageUrl ?: "图片地址缺失"))
        sb.append(String.format("------------简介----------------\n%s\n", this.Introduction ?: "商品简介缺失"))
        sb.append("------------特性----------------\n")
        this.Features?.forEach {
            if (!it.isNullOrBlank())
                sb.append("$it\n")
        }
        sb.append(String.format("------------价格----------------\n%s\n", this.Price ?: "商品价格缺失"))
        sb.append("******************************************************************\n")
        if (sb.isNotEmpty())
            return sb.toString()
        else
            return ""
    }
}