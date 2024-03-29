package ad.blocker.connect.android

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import ad.blocker.connect.extensions.asLiveData
import ad.blocker.connect.model.Proxy
import ad.blocker.connect.model.ProxyMapper
import ad.blocker.connect.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSettingsManager @Inject constructor(
    @ApplicationContext context: Context,
    private val proxyMapper: ProxyMapper,
    private val proxyUpdateNotifier: ProxyUpdateNotifier,
    private val appSettings: AppSettings
) {

    private val contentResolver by lazy { context.contentResolver }

    private val _proxySetting = MutableLiveData<Proxy>()
    val proxySetting = _proxySetting.asLiveData()

    init {
        updateProxyData()
    }

    fun enableProxy(proxy: Proxy) {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            proxy.toString()
        )
        appSettings.lastUsedProxy = proxy
        updateProxyData()
    }

    fun disableProxy() {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            Proxy.Disabled.toString()
        )
        updateProxyData()
    }

    private fun updateProxyData() {
        val proxySetting = Settings.Global.getString(contentResolver, Settings.Global.HTTP_PROXY)
        _proxySetting.value = proxyMapper.from(proxySetting)
        proxyUpdateNotifier.notifyProxyChanged()
    }
}
