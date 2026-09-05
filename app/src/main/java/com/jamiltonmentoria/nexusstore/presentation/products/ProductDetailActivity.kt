package com.jamiltonmentoria.nexusstore.presentation.products

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.jamiltonmentoria.nexusstore.R
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import com.jamiltonmentoria.nexusstore.databinding.ActivityProductDetailBinding
import com.jamiltonmentoria.nexusstore.util.FeedbackUtils
import com.jamiltonmentoria.nexusstore.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductViewModel by viewModels()
    private var currentQuantity = 1
    private var productId = -1
    private var isInCart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            viewModel.getProductById(productId)
        } else {
            finish()
        }

        setupToolbar()
        setupQuantitySelector()
        setupAddToCart()
        observeViewModel()
    }

    private fun setupQuantitySelector() {
        binding.btnPlusDetail.setOnClickListener {
            currentQuantity++
            binding.textQuantityDetail.text = currentQuantity.toString()
            if (isInCart) {
                viewModel.updateCart(productId, true, currentQuantity)
            }
        }

        binding.btnMinusDetail.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                binding.textQuantityDetail.text = currentQuantity.toString()
                if (isInCart) {
                    viewModel.updateCart(productId, true, currentQuantity)
                }
            } else if (isInCart) {
                isInCart = false
                viewModel.updateCart(productId, false, 0)
                resetAddToCartButton()
            }
        }
    }

    private fun resetAddToCartButton() {
        isInCart = false
        binding.btnAddToCartDetail.text = "Adicionar ao Carrinho"
        binding.btnAddToCartDetail.setIconResource(R.drawable.ic_cart)
        binding.btnAddToCartDetail.backgroundTintList = ContextCompat.getColorStateList(this, R.color.btn_carts)
        binding.btnAddToCartDetail.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnAddToCartDetail.iconTint = ContextCompat.getColorStateList(this, R.color.white)
    }

    private fun setupAddToCart() {
        binding.btnAddToCartDetail.setOnClickListener {
            if (!isInCart) {
                startAddToCartSequence()
            } else {
                // Remove from cart
                isInCart = false
                viewModel.updateCart(productId, false, 0)
                resetAddToCartButton()
            }
        }
    }

    private fun startAddToCartSequence() {
        binding.btnAddToCartDetail.isEnabled = false
        
        binding.btnAddToCartDetail.text = ""
        binding.btnAddToCartDetail.icon = null
        
        binding.progressAddToCartDetail.alpha = 0f
        binding.progressAddToCartDetail.visibility = View.VISIBLE
        binding.progressAddToCartDetail.animate()
            .alpha(1f)
            .setDuration(200)
            .start()

        binding.root.postDelayed({
            binding.progressAddToCartDetail.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.progressAddToCartDetail.visibility = View.GONE
                    
                    FeedbackUtils.playCheckSound(this)
                    binding.btnAddToCartDetail.setIconResource(R.drawable.ic_check)
                    binding.btnAddToCartDetail.iconGravity = com.google.android.material.button.MaterialButton.ICON_GRAVITY_TEXT_START
                    binding.btnAddToCartDetail.scaleX = 0.5f
                    binding.btnAddToCartDetail.scaleY = 0.5f
                    binding.btnAddToCartDetail.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(300)
                        .withEndAction {
                            binding.btnAddToCartDetail.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .start()
                        }
                        .start()

                    binding.root.postDelayed({
                        binding.btnAddToCartDetail.animate()
                            .scaleX(0.0f)
                            .scaleY(0.0f)
                            .setDuration(200)
                            .withEndAction {
                                isInCart = true
                                binding.btnAddToCartDetail.text = "No Carrinho"
                                binding.btnAddToCartDetail.setIconResource(R.drawable.ic_minus)
                                binding.btnAddToCartDetail.backgroundTintList = ContextCompat.getColorStateList(
                                    this, 
                                    android.R.color.holo_red_light
                                )
                                binding.btnAddToCartDetail.setTextColor(ContextCompat.getColor(this, R.color.white))
                                binding.btnAddToCartDetail.iconTint = ContextCompat.getColorStateList(this, R.color.white)
                                viewModel.updateCart(productId, true, currentQuantity)
                                binding.btnAddToCartDetail.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(200)
                                    .start()
                                binding.btnAddToCartDetail.isEnabled = true
                            }.start()
                    }, 1200)
                }.start()
        }, 1500)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.toolbarDetail.setNavigationOnClickListener { finish() }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetail.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { displayDetails(it) }
                        }
                        is Resource.Error -> {
                            Toast.makeText(this@ProductDetailActivity, resource.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            // Poderia adicionar um shimmer ou progress
                        }
                    }
                }
            }
        }
    }

    private fun displayDetails(product: ProductDto) {
        binding.textTitleDetail.text = product.title
        binding.textCategoryDetail.text = product.category
        binding.textPriceDetail.text = String.format(Locale.getDefault(), "$ %.2f", product.price)
        binding.textDescriptionDetail.text = product.description
        binding.ratingBar.rating = product.rating.toFloat()
        binding.imgProductDetail.load(product.thumbnail)

        isInCart = product.isInCart
        currentQuantity = if (product.quantityInCart > 0) product.quantityInCart else 1
        binding.textQuantityDetail.text = currentQuantity.toString()

        if (isInCart) {
            binding.btnAddToCartDetail.text = "No Carrinho"
            binding.btnAddToCartDetail.setIconResource(R.drawable.ic_minus)
            binding.btnAddToCartDetail.backgroundTintList = ContextCompat.getColorStateList(
                this,
                android.R.color.holo_red_light
            )
            binding.btnAddToCartDetail.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnAddToCartDetail.iconTint = ContextCompat.getColorStateList(this, R.color.white)
        } else {
            resetAddToCartButton()
        }
    }
}
