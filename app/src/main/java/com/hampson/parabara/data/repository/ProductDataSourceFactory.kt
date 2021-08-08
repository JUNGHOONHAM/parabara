package com.hampson.parabara.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable

class ProductDataSourceFactory (private val apiService: DBInterface, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Product>() {

    val productLiveDataSource = MutableLiveData<ProductDataSource>()

    override fun create(): DataSource<Int, Product> {
        val productDataSource = ProductDataSource(apiService, compositeDisposable)

        productLiveDataSource.postValue(productDataSource)
        return productDataSource
    }

}