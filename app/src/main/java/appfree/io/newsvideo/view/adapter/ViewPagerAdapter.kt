package appfree.io.newsvideo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import appfree.io.newsvideo.R
import appfree.io.newsvideo.data.NewsVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter(
    private val ldActiveVideo: LiveData<Int>,
    private val lifecycle: LifecycleOwner
) : RecyclerView.Adapter<ViewPagerAdapter.PagerVH>() {

    private val items = ArrayList<NewsVideo>()

    fun fetch(list: List<NewsVideo>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        holder.onAppear()
    }

    inner class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onAppear() {
            ldActiveVideo.observe(lifecycle, Observer { pos ->
                if (pos == adapterPosition) {
                    itemView.youtube_view.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                        override fun onYouTubePlayer(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                            youTubePlayer.cueVideo(items[adapterPosition].content!!, 0f)
                            youTubePlayer.play()
                        }

                    })
                }
            })
        }
    }
}

