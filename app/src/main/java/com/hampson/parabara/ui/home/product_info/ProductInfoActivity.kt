package com.hampson.parabara.ui.home.product_info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hampson.parabara.R
import com.hampson.parabara.data.api.DBClient
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.databinding.ActivityProductInfoBinding
import com.hampson.parabara.ui.home.product_info.update.ProductUpdateActivity


class ProductInfoActivity : AppCompatActivity() {

    private var mBinding : ActivityProductInfoBinding? = null

    private lateinit var viewModel: ProductInfoViewModel
    private lateinit var productInfoRepository: ProductInfoRepository
    private lateinit var apiService: DBInterface

    private var productId: Long = -1

    private val UPDATE_PRODUCT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBinding = binding

        if (intent.hasExtra("productId")) {
            productId = intent.getLongExtra("productId", -1)
        }

        apiService = DBClient.getClient(this)
        productInfoRepository = ProductInfoRepository(apiService)
        viewModel = getViewModel()

        viewModel.productLiveData.observe(this, {
            bindUI(it.title, it.price, it.content)

            for (image in it.images) {
                bindImage(image)
            }
        })

        viewModel.getDeleteLiveData().observe(this, {
            if (it.code == "SUCCESS" && it.status == 200) {
                Toast.makeText(this, "상품삭제가 완료되었습니다.", Toast.LENGTH_LONG).show()

                finish()
            }
        })

        viewModel.networkState.observe(this, {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.textViewError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })


        binding.buttonUpdate.setOnClickListener {
            Intent(this, ProductUpdateActivity()::class.java).apply {
                putExtra("productId", productId)
            }.run { startActivityForResult(this, UPDATE_PRODUCT) }
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteProduct(productId)
        }
    }

    private fun bindUI(title: String, price: Long?, content: String) {
        mBinding?.textViewProductName?.text = title
        mBinding?.textViewPrice?.text = price.toString() + "원"
        mBinding?.textViewContent?.text = content
    }

    private fun bindImage(data: String) {
        val imageView = ImageView(this)
        imageView.layoutParams = ViewGroup.LayoutParams(300, 300)
        Glide.with(this)
            .load(data)
            .circleCrop()
            .placeholder(R.drawable.ic_baseline_photo_camera_48)
            .into(imageView)
        mBinding?.layoutImageList?.addView(imageView)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_PRODUCT && resultCode == Activity.RESULT_OK) {
            bindUI(data?.getStringExtra("title").toString(), data?.getLongExtra("price", -1),
                data?.getStringExtra("content").toString())
        }
    }

    private fun getViewModel(): ProductInfoViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAS T")
                return ProductInfoViewModel(productInfoRepository, productId) as T
            }
        }).get(ProductInfoViewModel::class.java)
    }
}