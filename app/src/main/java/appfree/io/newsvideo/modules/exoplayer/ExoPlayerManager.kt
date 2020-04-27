package appfree.io.newsvideo.modules.exoplayer

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.net.toUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

object ExoPlayerManager {

    private var player: SimpleExoPlayer? = null

    fun setUp(context: Context?, pUrl: String?) {
        pUrl?.let { url ->
            context?.let {
                pause()
                releasePlayer()
                player = SimpleExoPlayer.Builder(context).build()
            }
            val mediaSource = MediaSourceBuilder.build(url.toUri())
            player?.prepare(mediaSource)
        }
    }

    fun bind(playerView: PlayerView) {
        playerView.player = player
    }

    fun bind(playerView: PlayerView, isAutoPlay: Boolean) {
        playerView.player = player
        if (isAutoPlay) {
            play()
        } else {
            pause()
        }
    }

    fun bind(playerView: PlayerView, isAutoPlay: Boolean, currentPlaybackMs: Long?) {
        if (player?.playbackState != Player.STATE_READY) {
            player?.seekTo(currentPlaybackMs ?: 0)
            playerView.player =
                player
            if (isAutoPlay) {
                play()
            } else {
                pause()
            }
        }
    }

    fun bind(playerView: PlayerView, currentPosition: Long) {
        player?.apply {
            play()
            playerView.player =
                player
            seekTo(currentPosition)
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi(view: View) {
        view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    fun play() {
        player?.playWhenReady = true
    }

    fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
        if (player != null) {
            player?.release()
            player = null
        }
    }

    fun pause() {
        player?.playWhenReady = false
    }

    fun stop() {
        player?.stop(true)
    }
}