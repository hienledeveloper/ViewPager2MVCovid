package appfree.io.newsvideo

import android.app.Application
import appfree.io.newsvideo.modules.networks.AndroidNetworkingManager

class ImApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworkingManager.initialize(applicationContext)
    }

}