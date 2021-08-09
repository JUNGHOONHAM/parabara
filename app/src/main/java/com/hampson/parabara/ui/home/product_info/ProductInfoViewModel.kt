package com.hampson.parabara.ui.home.product_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.DeleteResponse
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable

class ProductInfoViewModel (private val productInfoRepository: ProductInfoRepository, private val productId: Long) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val deleteProductLiveData: MediatorLiveData<DeleteResponse> = MediatorLiveData()

    val productLiveData : LiveData<Product> by lazy {
        productInfoRepository.getProduct(compositeDisposable, productId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        productInfoRepository.getNetworkState()
    }

    fun getDeleteLiveData() : LiveData<DeleteResponse> {
        return deleteProductLiveData
    }

    fun deleteProduct(productId: Long) {
        val repositoryLiveData: LiveData<DeleteResponse> =
            productInfoRepository.deleteProduct(compositeDisposable, productId)

        deleteProductLiveData.addSource(repositoryLiveData) {
            deleteProductLiveData.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}