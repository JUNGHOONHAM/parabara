package com.hampson.parabara.ui.home.product_info.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable

class ProductUpdateViewModel (private val productUpdateRepository: ProductUpdateRepository, private val productId: Long) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val updateProductLiveData: MediatorLiveData<Response> = MediatorLiveData()

    val productLiveData : LiveData<Product> by lazy {
        productUpdateRepository.getProduct(compositeDisposable, productId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        productUpdateRepository.getNetworkState()
    }

    fun getUpdateLiveData() : LiveData<Response> {
        return updateProductLiveData
    }

    fun updateProduct(id: Long, title: String, price: Long, content: String) {
        val repositoryLiveData: LiveData<Response> =
            productUpdateRepository.updateProduct(compositeDisposable, id, title, price, content)

        updateProductLiveData.addSource(repositoryLiveData) {
            updateProductLiveData.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}