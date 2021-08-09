package com.hampson.parabara.ui.home.product_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable

class ProductInfoViewModel (private val productInfoRepository: ProductInfoRepository, private val productId: Long) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val productLiveData : LiveData<Product> by lazy {
        productInfoRepository.getProduct(compositeDisposable, productId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        productInfoRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}