package com.hampson.parabara.ui.home.product_info

import android.util.Log
import androidx.lifecycle.LiveData
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.repository.ImageUploadNetworkDataSource
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.repository.ProductNetworkDataSource
import com.hampson.parabara.data.vo.DeleteResponse
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MultipartBody

class ProductInfoRepository (private val apiService : DBInterface) {

    lateinit var productNetworkDataSource: ProductNetworkDataSource

    fun getProduct (compositeDisposable: CompositeDisposable, productId: Long) : LiveData<Product> {
        productNetworkDataSource = ProductNetworkDataSource(apiService, compositeDisposable)
        productNetworkDataSource.getProduct(productId)

        return productNetworkDataSource.downloadedProduct
    }

    fun deleteProduct (compositeDisposable: CompositeDisposable, productId: Long) : LiveData<DeleteResponse> {
        productNetworkDataSource = ProductNetworkDataSource(apiService, compositeDisposable)
        productNetworkDataSource.deleteProduct(productId)

        return productNetworkDataSource.downloadedDeleteResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return productNetworkDataSource.networkState
    }
}