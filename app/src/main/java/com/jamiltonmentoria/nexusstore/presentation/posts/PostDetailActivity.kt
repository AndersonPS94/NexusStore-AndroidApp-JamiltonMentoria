package com.jamiltonmentoria.nexusstore.presentation.posts

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.jamiltonmentoria.nexusstore.R
import com.jamiltonmentoria.nexusstore.data.model.PostDto
import com.jamiltonmentoria.nexusstore.data.model.UserDto
import com.jamiltonmentoria.nexusstore.databinding.ActivityPostDetailBinding
import com.jamiltonmentoria.nexusstore.util.FeedbackUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostViewModel by viewModels()
    private var isLiked = false
    private var isSaved = false
    private var currentLikes = 0
    private var postId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("POST_DATA", PostDto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("POST_DATA") as? PostDto
        }

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER_DATA", UserDto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("USER_DATA") as? UserDto
        }

        if (post != null) {
            setupUI(post, user)
        } else {
            finish()
        }
    }

    private fun setupUI(post: PostDto, user: UserDto?) {
        setSupportActionBar(binding.toolbarPostDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Explorar"
        binding.toolbarPostDetail.setNavigationOnClickListener { finish() }

        postId = post.id
        isLiked = post.isLiked
        isSaved = post.isSaved
        currentLikes = (post.reactions?.likes ?: 0) + post.userLikesCount

        val postBinding = binding.includedPost
        
        // Update initial icons
        if (isLiked) postBinding.btnLike.setColorFilter(ContextCompat.getColor(this, R.color.btn_posts))
        if (isSaved) postBinding.btnSave.setColorFilter(ContextCompat.getColor(this, R.color.yellow))

        val username = if (user != null) {
            "${user.firstName.lowercase()}_${user.lastName.lowercase()}"
        } else {
            String.format(Locale.getDefault(), "nexus_user_%d", post.userId)
        }
        
        postBinding.textUserName.text = username
        postBinding.textPostContent.text = String.format(Locale.getDefault(), "%s %s", username, post.body)
        
        postBinding.imgUserAvatar.load(user?.image) {
            placeholder(R.drawable.ic_users)
            error(R.drawable.ic_users)
            transformations(CircleCropTransformation())
        }

        postBinding.imgPostMedia.load("https://picsum.photos/seed/${post.id}/800/800")

        val views = post.views

        fun updateStats() {
            postBinding.textPostStats.text = String.format(Locale.getDefault(), "%d curtidas • %d visualizações", currentLikes, views)
        }

        updateStats()

        postBinding.btnLike.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                currentLikes++
                postBinding.btnLike.setColorFilter(ContextCompat.getColor(this, R.color.btn_posts))
                FeedbackUtils.playLikeSound(this)
            } else {
                currentLikes--
                postBinding.btnLike.clearColorFilter()
            }
            updateStats()
            viewModel.updateLike(postId, isLiked, if (isLiked) 1 else 0)
        }

        postBinding.btnSave.setOnClickListener {
            isSaved = !isSaved
            if (isSaved) {
                postBinding.btnSave.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
                FeedbackUtils.playFavoriteSound(this)
            } else {
                postBinding.btnSave.clearColorFilter()
            }
            viewModel.updateSave(postId, isSaved)
        }
    }
}
