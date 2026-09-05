package com.jamiltonmentoria.nexusstore.presentation.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.jamiltonmentoria.nexusstore.R
import com.jamiltonmentoria.nexusstore.data.model.PostDto
import com.jamiltonmentoria.nexusstore.data.model.UserDto
import com.jamiltonmentoria.nexusstore.databinding.ItemPostListBinding
import java.util.Locale

class PostAdapter(
    private val onClick: (PostDto) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var posts = listOf<PostDto>()
    private var usersMap = mapOf<Int, UserDto>()

    fun updateData(newPosts: List<PostDto>, newUsers: Map<Int, UserDto>) {
        posts = newPosts
        usersMap = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(private val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostDto) {
            val user = usersMap[post.userId]
            val username = if (user != null) {
                "${user.firstName.lowercase()}_${user.lastName.lowercase()}"
            } else {
                String.format(Locale.getDefault(), "nexus_user_%d", post.userId)
            }
            
            binding.textUserList.text = username
            binding.textTitleList.text = post.title
            binding.textBodyPreview.text = post.body

            binding.imgUserList.load(user?.image) {
                crossfade(enable = true)
                placeholder(R.drawable.ic_users)
                error(R.drawable.ic_users)
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener {
                onClick(post)
            }
        }
    }
}
