package com.hampson.parabara.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.api.FIRST_PAGE
import com.hampson.parabara.data.api.POST_SIZE
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductDataSource (private val apiService : DBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Product>() {

    private var page = FIRST_PAGE
    private var size = POST_SIZE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getProducts(params.key, size)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.product.total >= params.key) {
                            callback.onResult(it.product.rows, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {

    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Product>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getProducts(page, size)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.product.rows, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
    }

}