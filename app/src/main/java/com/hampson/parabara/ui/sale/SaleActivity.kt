package com.hampson.parabara.ui.sale

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hampson.parabara.data.api.DBClient
import com.hampson.parabara.data.api.DBInterface
import com.hampson.parabara.databinding.ActivitySaleBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SaleActivity : AppCompatActivity() {

    private var mBinding : ActivitySaleBinding? = null

    private val PICK_FROM_ALBUM = 10

    private lateinit var viewModel: SaleViewModel
    private lateinit var saleRepository: SaleRepository
    private lateinit var apiService: DBInterface

    private val selectImageList: ArrayList<Long> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBinding = binding

        apiService = DBClient.getClient(this)
        saleRepository = SaleRepository(apiService)
        viewModel = getViewModel()

        viewModel.getImageUploadResponse().observe(this, {
            selectImageList.add(it.id)
            bindImage(it.url)
        })

        viewModel.getCreateProductResponse().observe(this, {
            if (it.code == "SUCCESS" && it.status == 200) {
                Toast.makeText(this, "상품등록이 완료되었습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        })

        binding.layoutCamera.setOnClickListener {
            if (isStoragePermissionGranted()) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent, PICK_FROM_ALBUM)
            }
        }

        binding.buttonSave.setOnClickListener {
            if (!validationCheck()) { return@setOnClickListener }

            val title = mBinding?.editTextTitle?.text.toString()
            val price = mBinding?.editTextPrice?.text.toString().toLong()
            val content = mBinding?.editTextContent?.text.toString()

            viewModel.createProduct(title, price, content, selectImageList)
        }
    }

    private fun validationCheck(): Boolean {

        if (selectImageList.size == 0) {
            Toast.makeText(this, "사진을 선택해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }

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

    private fun getPathFromUri(uri: Uri?): String? {
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            this.contentResolver?.query(uri!!, projection, null, null, null)
        cursor?.moveToNext()
        val index = cursor?.getColumnIndex(MediaStore.MediaColumns.DATA)
        val path = cursor?.getString(index!!)

        cursor?.close()

        return path
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                return false
            }
        } else {
            return true
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val file = File(getPathFromUri(data?.data))

            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)

            viewModel.imageUpload(imageFile)
        }
    }

    private fun bindImage(data: String) {
        val imageView = ImageView(this)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        Glide.with(this)
            .load(data)
            .into(imageView)
        mBinding?.layoutImageList?.addView(imageView)
    }

    private fun getViewModel(): SaleViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAS T")
                return SaleViewModel(saleRepository) as T
            }
        }).get(SaleViewModel::class.java)
    }

}