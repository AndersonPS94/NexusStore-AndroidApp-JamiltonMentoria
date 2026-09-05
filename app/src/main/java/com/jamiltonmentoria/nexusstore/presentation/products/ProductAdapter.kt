package com.jamiltonmentoria.nexusstore.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.core.content.ContextCompat
import coil.load
import com.jamiltonmentoria.nexusstore.R
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import com.jamiltonmentoria.nexusstore.databinding.ItemProductBinding
import com.jamiltonmentoria.nexusstore.util.FeedbackUtils
import java.util.Locale

class ProductAdapter(
    private val onAddToCart: (Int, Int) -> Unit,
    private val onClick: (ProductDto) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products = listOf<ProductDto>()

    fun updateList(newList: List<ProductDto>) {
        products = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDto) {
            binding.textTitle.text = product.title
            binding.textCategory.text = product.category
            binding.textPrice.text = String.format(Locale.getDefault(), "$ %.2f", product.price)
            binding.textRating.text = String.format(Locale.getDefault(), "★ %.1f", product.rating)
            binding.imgProduct.load(product.thumbnail)

            updateCartUI(product)
            
            binding.root.setOnClickListener {
                onClick(product)
            }

            binding.btnAddToCart.setOnClickListener {
                if (!product.isInCart) {
                    startAddToCartSequence(product)
                } else {
                    onAddToCart(product.id, 0)
                }
            }

            binding.btnPlus.setOnClickListener {
                onAddToCart(product.id, product.quantityInCart + 1)
            }

            binding.btnMinus.setOnClickListener {
                if (product.quantityInCart > 1) {
                    onAddToCart(product.id, product.quantityInCart - 1)
                } else {
                    onAddToCart(product.id, 0)
                }
            }
        }

        private fun updateCartUI(product: ProductDto) {
            if (product.isInCart) {
                binding.frameCart.visibility = View.GONE
                binding.layoutQuantity.visibility = View.VISIBLE
                binding.textQuantity.text = product.quantityInCart.toString()
            } else {
                binding.frameCart.visibility = View.VISIBLE
                binding.layoutQuantity.visibility = View.GONE
                binding.progressAddToCart.visibility = View.GONE
                binding.btnAddToCart.setIconResource(R.drawable.ic_cart)
                binding.btnAddToCart.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.btn_carts)
            }
        }

        private fun startAddToCartSequence(product: ProductDto) {
            binding.btnAddToCart.isEnabled = false
            
            binding.btnAddToCart.icon = null
            binding.progressAddToCart.alpha = 0f
            binding.progressAddToCart.visibility = View.VISIBLE
            binding.progressAddToCart.animate()
                .alpha(1f)
                .setDuration(200)
                .start()

            binding.root.postDelayed({
                binding.progressAddToCart.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        binding.progressAddToCart.visibility = View.GONE
                        
                        FeedbackUtils.playCheckSound(binding.root.context)
                        binding.btnAddToCart.setIconResource(R.drawable.ic_check)
                        binding.btnAddToCart.scaleX = 0.5f
                        binding.btnAddToCart.scaleY = 0.5f
                        binding.btnAddToCart.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(300)
                            .withEndAction {
                                binding.btnAddToCart.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(100)
                                    .start()
                            }
                            .start()

                        binding.root.postDelayed({
                            onAddToCart(product.id, 1)
                            binding.btnAddToCart.isEnabled = true
                        }, 800)
                    }.start()
            }, 1000)
        }
    }
}
