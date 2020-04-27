package appfree.io.newsvideo.ui.main

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import appfree.io.newsvideo.R
import appfree.io.newsvideo.data.NewsVideo
import appfree.io.newsvideo.modules.exoplayer.ExoPlayerManager
import appfree.io.newsvideo.view.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val model: MainVM by viewModels()

    private var mTotal = 0
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set fullscreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // keep screen on start
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                acquire(15*60*1000L /*15 minutes*/)
            }
        }
        setContentView(R.layout.activity_main)
        adapter = ViewPagerAdapter(
            model.ldActiveVideo,
            this
        )
        viewPager2.adapter = adapter
        model.ldNewsVideos.observe(this, Observer { videos ->
            mTotal = videos.size
            Collections.shuffle(videos)
            adapter.fetch(videos)
        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                model.ldActiveVideo.postValue(position)
                if (position==mTotal-2) {
                    model.fetch()
                }
            }
        })
        model.fetch()
    }
}
