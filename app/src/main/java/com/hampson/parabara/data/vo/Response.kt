package com.hampson.parabara.data.vo

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("code")
    val code: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val product: Product,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)