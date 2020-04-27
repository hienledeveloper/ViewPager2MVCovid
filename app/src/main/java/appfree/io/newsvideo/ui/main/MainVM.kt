package appfree.io.newsvideo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import appfree.io.newsvideo.data.NewsVideo
import appfree.io.newsvideo.modules.networks.AndroidNetworkingManager

class MainVM: ViewModel() {

    val ldNewsVideos = MutableLiveData<List<NewsVideo>>()
    val ldError = MutableLiveData<String>()
    val ldActiveVideo = MutableLiveData<Int>()

    fun fetch() {
        AndroidNetworkingManager.getNewsVideo { list, error ->
            if (error.isNullOrEmpty()) {
                ldNewsVideos.postValue(list)
            } else {
                ldError.postValue(error)
            }
        }
    }

}