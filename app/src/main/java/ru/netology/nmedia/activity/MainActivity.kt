package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
//        val adapter = PostsAdapter(
//            onLikeListener = { viewModel.likeById(it.id) },
//            onShareListener = { viewModel.shareById(it.id) },
//            onRemoveListener = { viewModel.removeById(it.id) }
//        )

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                binding.groupEdit.visibility = View.VISIBLE
                viewModel.edit(post)
            }
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            //adapter.list = posts             //обновление данных
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()         //как только edited меняется, обновляем текст
                setText(post.content)  //как только edited меняется, обновляем текст
            }
            with(binding.groupText) {
                setText(post.content)  //как только edited меняется,
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
//                binding.groupEdit.setOnClickListener{
//                    binding.groupEdit.visibility = View.GONE
//                }
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.groupEdit.visibility = View.GONE
            }
        }

        binding.content.setOnClickListener{
            binding.groupEdit.visibility = View.VISIBLE
        }

        binding.clear.setOnClickListener {
            with(binding.content) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.groupEdit.visibility = View.GONE
            }
        }

    }
}