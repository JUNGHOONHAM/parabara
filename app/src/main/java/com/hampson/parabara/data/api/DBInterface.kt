package com.hampson.parabara.data.api

import com.hampson.parabara.data.repository.Response
import io.reactivex.Single
import retrofit2.http.*

interface DBInterface {

    @POST("api/v2/sms_auth/send_sms")
    fun sendPhoneNumber(@Query("phone_number") phoneNumber: String): Single<Response>

    @POST("api/v2/sms_auth/verified")
    fun sendVerifiedNumber(@Query("phone_number") phoneNumber: String, @Query("token") token: String, @Query("verified_number") verifiedNumber: String): Single<Response>


}