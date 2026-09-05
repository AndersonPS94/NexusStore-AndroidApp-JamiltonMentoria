package com.jamiltonmentoria.nexusstore.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import com.jamiltonmentoria.nexusstore.data.model.ProductResponse
import com.jamiltonmentoria.nexusstore.data.repository.ProductRepository
import com.jamiltonmentoria.nexusstore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<ProductResponse>>(Resource.Loading())
    val products: StateFlow<Resource<ProductResponse>> = _products

    private val _productDetail = MutableStateFlow<Resource<ProductDto>>(Resource.Loading())
    val productDetail: StateFlow<Resource<ProductDto>> = _productDetail

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts().collect {
                _products.value = it
            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id).collect {
                _productDetail.value = it
            }
        }
    }

    fun updateCart(productId: Int, isInCart: Boolean, qty: Int) {
        viewModelScope.launch {
            repository.updateCartStatus(productId, isInCart, qty)
        }
    }
}
