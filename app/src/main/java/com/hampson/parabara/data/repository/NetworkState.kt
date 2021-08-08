package com.hampson.parabara.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "성공")
            LOADING = NetworkState(Status.RUNNING, "로딩중")
            ERROR = NetworkState(Status.FAILED, "오류")
            ENDOFLIST = NetworkState(Status.FAILED, "불러올 데이터가 없습니다.")
        }
    }
}