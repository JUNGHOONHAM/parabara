package com.hampson.parabara.ui.home.product_info

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hampson.parabara.R
import com.hampson.parabara.data.api.DBClient
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.databinding.ActivityProductInfoBinding


class ProductInfoActivity : AppCompatActivity() {

    private var mBinding : ActivityProductInfoBinding? = null

    private lateinit var viewModel: ProductInfoViewModel
    private lateinit var productInfoRepository: ProductInfoRepository
    private lateinit var apiService: DBInterface

    private var productId: Long = -1

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
            binding.textViewProductName.text = it.title
            binding.textViewPrice.text = it.price.toString() + "원"
            binding.textViewContent.text = it.content

            for (image in it.images) {
                bindImage(image)
            }
        })

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

    private fun getViewModel(): ProductInfoViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAS T")
                return ProductInfoViewModel(productInfoRepository, productId) as T
            }
        }).get(ProductInfoViewModel::class.java)
    }

}