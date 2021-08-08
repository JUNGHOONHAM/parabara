package com.hampson.parabara.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.data.vo.Response
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.lang.Exception

class ProductNetworkDataSource (private val apiService : DBInterface, private val compositeDisposable : CompositeDisposable) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedResponse = MutableLiveData<Response>()
    val downloadedResponse: MutableLiveData<Response>
        get() = _downloadedResponse

    fun createProduct(title: String, price: Long, content: String, images: ArrayList<Long>) {
        try {
            compositeDisposable.add(
                apiService.createProduct(title, price, content, images)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (e: Exception) {

        }
    }
}