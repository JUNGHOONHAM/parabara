package com.hampson.parabara.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hampson.parabara.R
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import com.hampson.parabara.databinding.ItemProductListBinding
import com.hampson.parabara.databinding.NetworkStateItemBinding

class ProductPagedListAdapter(public val context: Context) : PagedListAdapter<Product, RecyclerView.ViewHolder>(
    ProductDiffCallback()
) {

    val PRODUCT_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == PRODUCT_VIEW_TYPE) {
            (holder as ProductItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            PRODUCT_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == PRODUCT_VIEW_TYPE) {
            val binding = ItemProductListBinding.inflate(layoutInflater)
            return ProductItemViewHolder(
                binding
            )
        } else {
            val binding = NetworkStateItemBinding.inflate(layoutInflater)
            return NetworkStateItemViewHolder(
                binding
            )
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    class ProductItemViewHolder (private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product?, context: Context) {
            binding.textViewProductName.text = product?.title
            binding.textViewPrice.text = product?.price.toString() + "원"

            Glide.with(context)
                .load(product?.images?.get(0))
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_photo_camera_48)
                .into(binding.imageView)

            itemView.setOnClickListener {
                Intent(context, JobInfoActivity::class.java).apply {
                    putExtra("productId", job?.id)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    class NetworkStateItemViewHolder (private val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {

            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.textViewErrorMessage.visibility = View.VISIBLE
                binding.textViewErrorMessage.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.textViewErrorMessage.visibility = View.GONE
                // textViewErrorMessage.text = networkState.msg
            } else {
                binding.textViewErrorMessage.visibility = View.GONE
            }

        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}