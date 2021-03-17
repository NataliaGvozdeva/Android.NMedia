package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)   //????
        setContentView(binding.root)  //????

        val post = Post(
            id = 1,
            author = "Нетология. Университет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likesSum = 9_999_999,
            sharedSum = 1_999_999,
            viewSum = 500_999_999
        )

        with(binding) {
            textAuthor.text = post.author
            textPublished.text = post.published
            content.text = post.content
            likesNumber.text = displayNumbers(post.likesSum)
            shareNumber.text = displayNumbers(post.sharedSum)
            viewNumber.text = displayNumbers(post.viewSum++)
            view.setImageResource(R.drawable.ic_baseline_visibility_viewed_24)

            if (post.likedByMe) {
                like?.setImageResource(R.drawable.ic_liked_24)
            }
            if (post.sharedSum > 0) {
                share.setImageResource(R.drawable.ic_baseline_shared_24)
            }

            like?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24
                )
                if (post.likedByMe) post.likesSum++ else post.likesSum--
                likesNumber.text = displayNumbers(post.likesSum)

//                Log.d("Tag", "done_Like")
            }

            share?.setOnClickListener {
                shareNumber.text = displayNumbers(post.sharedSum++)
//                Log.d("Tag", "done_Repost")
            }

//            root?.setOnClickListener {
//                Log.d("Tag", "done_root")
//            }
//
//            imageView?.setOnClickListener {
//               Log.d("Tag", "Avatar")
//            }

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
            //in 10_000_000..999_999_999 -> "${number / 1_000_000}M"
            //else -> "a lot"
        }
    }

}
