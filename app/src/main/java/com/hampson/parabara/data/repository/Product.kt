package com.hampson.parabara.data.repository

data class Product(
    val id: Int,
    val title: String,
    val content: String,
    val price: Int,
    val images: List<String>
)