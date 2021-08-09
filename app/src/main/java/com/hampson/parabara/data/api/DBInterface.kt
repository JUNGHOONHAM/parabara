package com.hampson.parabara.data.api

import com.hampson.parabara.data.vo.Response
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface DBInterface {

    @Multipart
    @POST("api/product/upload")
    fun imageUpload(@Part image: MultipartBody.Part): Single<Response>

    @POST("api/product")
    fun createProduct(@Query("title") title: String, @Query("price") price: Long,
                      @Query("content") content: String, @Query("images") images: ArrayList<Long>): Single<Response>

    @GET("api/product")
    fun getProducts(@Query("page") page: Int, @Query("size") size: Int): Single<Response>

    @GET("api/product/{id}")
    fun getProduct(@Path("id") id: Long): Single<Response>

    @PUT("api/product")
    fun updateProduct(@Query("id") id: Long, @Query("title") title: String,
                      @Query("price") price: Long, @Query("content") content: String): Single<Response>

}