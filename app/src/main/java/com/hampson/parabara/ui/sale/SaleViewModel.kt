package com.hampson.parabara.ui.sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MultipartBody

class SaleViewModel (private val saleRepository: SaleRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val imageUploadLiveData: MediatorLiveData<Product> = MediatorLiveData()
    private val createProductLiveData: MediatorLiveData<Response> = MediatorLiveData()

    fun getImageUploadResponse() : LiveData<Product> {
        return imageUploadLiveData
    }

    fun getCreateProductResponse() : LiveData<Response> {
        return createProductLiveData
    }

    val networkState : LiveData<NetworkState> by lazy {
        saleRepository.getNetworkState()
    }

    fun imageUpload(imageFile: MultipartBody.Part) {
        val repositoryLiveData: LiveData<Product> =
            saleRepository.imageUpload(compositeDisposable, imageFile)

        imageUploadLiveData.addSource(repositoryLiveData) { value: Product ->
            imageUploadLiveData.setValue(value)
        }
    }

    fun createProduct(title: String, price: Long, content: String, images: ArrayList<Long>) {
        val repositoryLiveData: LiveData<Response> =
            saleRepository.createProduct(compositeDisposable, title, price, content, images)

        createProductLiveData.addSource(repositoryLiveData) { value: Response ->
            createProductLiveData.value = value
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}