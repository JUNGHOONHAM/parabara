package com.hampson.parabara.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel (private val homeRepository: HomeRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val productLiveData : LiveData<PagedList<Product>> by lazy {
        homeRepository.getProductPagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        homeRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return productLiveData.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}