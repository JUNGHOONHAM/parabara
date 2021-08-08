package com.hampson.parabara.ui.sale

import androidx.lifecycle.LiveData
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.repository.ImageUploadNetworkDataSource
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.repository.ProductNetworkDataSource
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MultipartBody

class SaleRepository (private val apiService : DBInterface) {

    lateinit var imageUploadNetworkDataSource: ImageUploadNetworkDataSource
    lateinit var productNetworkDataSource: ProductNetworkDataSource

    fun imageUpload (compositeDisposable: CompositeDisposable, imageFile: MultipartBody.Part) : LiveData<Product> {
        imageUploadNetworkDataSource = ImageUploadNetworkDataSource(apiService, compositeDisposable)
        imageUploadNetworkDataSource.imageUpload(imageFile)

        return imageUploadNetworkDataSource.downloadedResponse
    }

    fun createProduct (compositeDisposable: CompositeDisposable, title: String, price: Long, content: String, images: ArrayList<Long>) : LiveData<Response> {
        productNetworkDataSource = ProductNetworkDataSource(apiService, compositeDisposable)
        productNetworkDataSource.createProduct(title, price, content, images)

        return productNetworkDataSource.downloadedResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return imageUploadNetworkDataSource.networkState
    }
}