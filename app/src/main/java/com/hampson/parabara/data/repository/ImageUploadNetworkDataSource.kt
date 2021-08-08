package com.hampson.parabara.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.lang.Exception

class ImageUploadNetworkDataSource (private val apiService : DBInterface, private val compositeDisposable : CompositeDisposable) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedResponse = MutableLiveData<Product>()
    val downloadedResponse: MutableLiveData<Product>
        get() = _downloadedResponse

    fun imageUpload(imageFile: MultipartBody.Part) {
        try {
            compositeDisposable.add(
                apiService.imageUpload(imageFile)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedResponse.postValue(it.product)
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