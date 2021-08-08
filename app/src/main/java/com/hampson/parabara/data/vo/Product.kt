package com.hampson.parabara.data.vo

data class Product(
    val id: Long,
    val title: String,
    val content: String,
    val price: Long,
    val images: List<String>,

    val rows: List<Product>,

    // image
    val url: String,

    // meta
    val page: Int,
    val total: Int,
    val records: Int,
)