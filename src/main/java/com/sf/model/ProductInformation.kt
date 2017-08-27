package com.sf.model

/**
 * Created by Shenfan on 2017/7/14.
 * All Rights Reserved
 */

/**
 *
 * 商品信息模型类
 */
class ProductInformation {
    var ProductName: String? = ""
    var Introduction: String? = ""
    var Features: List<String>? = listOf()
    var ImageUrl: String? = ""
    var Price: String? = ""

    fun getInfoString(): String {
        val sb = StringBuilder()
        sb.append("----------商品名称------------\n${this.ProductName ?: "商品名字缺失"}\n")
        sb.append("----------图片网址------------\n${this.ImageUrl ?: "图片地址缺失"}\n")
        sb.append("------------简介----------------\n${this.Introduction ?: "商品简介缺失"}\n")
        sb.append("------------特性----------------\n")
        this.Features?.forEach {
            if (!it.isBlank())
                sb.append("${it.trim()}\n")
        }
        sb.append("------------价格----------------\n${this.Price ?: "商品价格缺失"}\n")
        sb.append("******************************************************************\n")
        return if (sb.isNotEmpty()) sb.toString() else ""
    }
}