package com.jamiltonmentoria.nexusstore.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jamiltonmentoria.nexusstore.R
import com.jamiltonmentoria.nexusstore.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnProducts.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productListFragment)
        }
        binding.btnCarts.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cartListFragment)
        }
        binding.btnUsers.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userListFragment)
        }
        binding.btnPosts.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
