package com.jamiltonmentoria.nexusstore.presentation.posts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamiltonmentoria.nexusstore.databinding.ActivityPostListBinding
import com.jamiltonmentoria.nexusstore.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostListBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter by lazy {
        PostAdapter { post ->
            val user = viewModel.usersMap.value[post.userId]
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("POST_DATA", post)
                putExtra("USER_DATA", user)
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbarPosts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPosts.setNavigationOnClickListener { finish() }
        
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(viewModel.posts, viewModel.usersMap) { postsRes, usersMap ->
                    Pair(postsRes, usersMap)
                }.collect { (postsRes, usersMap) ->
                    when (postsRes) {
                        is Resource.Success -> {
                            binding.progressBarPosts.visibility = View.GONE
                            postsRes.data?.let { 
                                adapter.updateData(it.posts, usersMap) 
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBarPosts.visibility = View.GONE
                            Toast.makeText(this@PostListActivity, postsRes.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            binding.progressBarPosts.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}
