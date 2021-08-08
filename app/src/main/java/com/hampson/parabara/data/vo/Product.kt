package com.hampson.parabara.data.vo

data class Product(
    val id: Long,
    val title: String,
    val content: String,
    val price: Long,
    val images: List<String>,

    // image
    val url: String,
)