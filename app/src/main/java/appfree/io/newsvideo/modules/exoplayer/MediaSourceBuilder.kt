package appfree.io.newsvideo.modules.exoplayer

import android.net.Uri
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.drm.ExoMediaCrypto
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

object MediaSourceBuilder {

    const val USER_AGENT = "Sen"
    const val FORMAT_MP4 = ".mp4"
    const val FORMAT_MP3 = ".mp3"
    const val FORMAT_M3U8 = "m3u8"

    //Build various MediaSource depending upon the type of Media for a given video/audio uri
    fun build(uri: Uri): MediaSource {
        val userAgent = USER_AGENT
        val lastPath = uri.lastPathSegment?:""

        val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)

        if(lastPath.contains(FORMAT_MP3) || lastPath.contains(
                FORMAT_MP4)){

            return ExtractorMediaSource.Factory(defaultHttpDataSourceFactory)
                .createMediaSource(uri)

        } else if(lastPath.contains(FORMAT_M3U8)){
            //hls
            val drmSessionManager: DrmSessionManager<ExoMediaCrypto> = DrmSessionManager.getDummyDrmSessionManager()
            return HlsMediaSource.Factory(defaultHttpDataSourceFactory)
                .setDrmSessionManager(drmSessionManager)
                .createMediaSource(uri)


        }else{
            //dash
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(defaultHttpDataSourceFactory)

            return DashMediaSource.Factory(dashChunkSourceFactory, defaultHttpDataSourceFactory)
                .createMediaSource(uri)

        }
    }

}