package com.hampson.parabara.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hampson.parabara.data.api.DBClient
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var mBinding : FragmentHomeBinding? = null

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeRepository: HomeRepository

    private lateinit var adapter: ProductPagedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        val apiService : DBInterface = DBClient.getClient(requireActivity())
        homeRepository = HomeRepository(apiService)
        viewModel = getViewModel()

        setRecyclerView()

        viewModel.productLiveData.observe(requireActivity(), {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(requireActivity(), {
            mBinding?.progressBar?.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            mBinding?.textViewError?.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                adapter.setNetworkState(it)
            }
        })

        return mBinding?.root
    }

    private fun setRecyclerView() {
        adapter = ProductPagedListAdapter(requireActivity())
        val layout = LinearLayoutManager(requireActivity())
        mBinding?.recyclerView?.layoutManager = layout
        mBinding?.recyclerView?.adapter = adapter
    }

    private fun getViewModel(): HomeViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(homeRepository) as T
            }
        }).get(HomeViewModel::class.java)
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}