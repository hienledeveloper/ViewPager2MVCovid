package appfree.io.newsvideo.modules.networks

import android.content.Context
import appfree.io.newsvideo.data.NewsVideo
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.jacksonandroidnetworking.JacksonParserFactory
import okhttp3.OkHttpClient


object AndroidNetworkingManager {

    fun initialize(context: Context) {
        val okHttpClient = OkHttpClient().newBuilder()
            .build();
        AndroidNetworking.initialize(context,okHttpClient)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun getNewsVideo(callback: (List<NewsVideo>, String?) -> Unit) {
        AndroidNetworking.get("https://goo-90ce8.firebaseapp.com/newsvideo/news.json")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(
                NewsVideo::class.java,
                object :
                    ParsedRequestListener<List<NewsVideo>> {
                    override fun onResponse(news: List<NewsVideo>) { // do anything with response
                        val list = ArrayList<NewsVideo>()
                        news.forEach { new ->
                            new.content?.let { content ->
                                if (!DataManager.contains(content)) {
                                    DataManager.add(content)
                                    list.add(new)
                                }
                            }
                        }
                        callback.invoke(list, null)
                    }

                    override fun onError(anError: ANError) { // handle error
                        callback.invoke(emptyList(), anError.errorBody)
                    }
                })
    }

}