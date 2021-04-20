package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat

typealias OnLikeListener = (post: Post) -> Unit  //тип для callback'а
typealias OnShareListener = (post: Post) -> Unit  //тип для callback'а

class PostsAdapter(private val onLikeListener: OnLikeListener,
                   private val onShareListener: OnShareListener
)
    //: RecyclerView.Adapter<PostViewHolder>() {
    : ListAdapter<Post, PostViewHolder>(PostDiffCallback()){
//    var list = emptyList<Post>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()   //данные (notifyDataSetChanged уведомляет адаптер об изменении данных)
//        }                            //что приводит к перерисовке списка

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //val post = list[position]
        val post = getItem(position)
        holder.bind(post)
    }

    //override fun getItemCount(): Int = list.size
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post,newItem: Post) : Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post,newItem: Post) : Boolean {
        return oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            likesSum.text = displayNumbers(post.likesSum)

            share.setImageResource(
                if (post.sharedSum > 0) R.drawable.ic_baseline_shared_24 else R.drawable.ic_baseline_share_24
            )
            sharedSum.text = displayNumbers(post.sharedSum)

            viewNumber.text = displayNumbers(post.viewSum)
            view.setImageResource(R.drawable.ic_baseline_visibility_viewed_24)

//            if (post.likedByMe) {
//                like.setImageResource(R.drawable.ic_liked_24)
//            }
            like.setOnClickListener{
                onLikeListener(post)
            }

            share.setOnClickListener{
                onShareListener(post)
            }
        }
    }

    fun displayNumbers(number: Long): String {
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.roundingMode = RoundingMode.DOWN
        return when (number) {
            in 0..999 -> "$number"
            in 1000..9_999 -> "${decimalFormat.format(number.toFloat() / 1_000)}K"
            in 10_000..999_999 -> "${number / 1_000}K"
            in 1_000_000..9_999_999 -> "${decimalFormat.format(number.toFloat() / 1_000_000)}M"
            else -> "${number / 1_000_000}M"
        }
    }

}