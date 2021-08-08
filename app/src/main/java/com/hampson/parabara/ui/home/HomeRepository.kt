package com.hampson.parabara.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.api.POST_SIZE
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.repository.ProductDataSource
import com.hampson.parabara.data.repository.ProductDataSourceFactory
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable

class HomeRepository (private val apiService : DBInterface) {

    lateinit var productPagedList: LiveData<PagedList<Product>>
    lateinit var productDataSourceFactory: ProductDataSourceFactory

    fun getProductPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Product>> {
        productDataSourceFactory = ProductDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_SIZE)
            .build()

        productPagedList = LivePagedListBuilder(productDataSourceFactory, config).build()

        return productPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<ProductDataSource, NetworkState>(
            productDataSourceFactory.productLiveDataSource, ProductDataSource::networkState
        )
    }
}