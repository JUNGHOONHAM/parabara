package com.hampson.parabara.ui.home.product_info.update

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hampson.parabara.data.api.DBClient
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Response
import com.hampson.parabara.databinding.ActivityProductUpdateBinding


class ProductUpdateActivity() : AppCompatActivity() {

    private var mBinding : ActivityProductUpdateBinding? = null

    private lateinit var viewModel: ProductUpdateViewModel
    private lateinit var productUpdateRepository: ProductUpdateRepository
    private lateinit var apiService: DBInterface

    private var productId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProductUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBinding = binding

        if (intent.hasExtra("productId")) {
            productId = intent.getLongExtra("productId", -1)
        }

        apiService = DBClient.getClient(this)
        productUpdateRepository = ProductUpdateRepository(apiService)
        viewModel = getViewModel()

        viewModel.productLiveData.observe(this, {
            binding.editTextTitle.setText(it.title)
            binding.editTextPrice.setText(it.price.toString())
            binding.editTextContent.setText(it.content)
        })

        viewModel.getUpdateLiveData().observe(this, {
            if (it.code == "SUCCESS" && it.status == 200) {
                Toast.makeText(this, "상품수정이 완료되었습니다.", Toast.LENGTH_LONG).show()

                setResult(it)
                finish()
            }
        })

        viewModel.networkState.observe(this, {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.textViewError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        binding.buttonSave.setOnClickListener {
            if (!validationCheck()) { return@setOnClickListener }

            val title = binding.editTextTitle.text.toString()
            val price = binding.editTextPrice.text.toString().toLong()
            val content = binding.editTextContent.text.toString()

            viewModel.updateProduct(productId, title, price, content)
        }
    }

    private fun validationCheck(): Boolean {

        if (mBinding?.editTextTitle?.text.toString() == "") {
            Toast.makeText(this, "상품명을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }

        if (mBinding?.editTextPrice?.text.toString() == "") {
            Toast.makeText(this, "판매가격을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }

        if (mBinding?.editTextContent?.text.toString() == "") {
            Toast.makeText(this, "상품내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun setResult(response: Response) {
        val intent = Intent()
        intent.putExtra("title", response.product.title)
        intent.putExtra("price", response.product.price)
        intent.putExtra("content", response.product.content)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun getViewModel(): ProductUpdateViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAS T")
                return ProductUpdateViewModel(productUpdateRepository, productId) as T
            }
        }).get(ProductUpdateViewModel::class.java)
    }
}