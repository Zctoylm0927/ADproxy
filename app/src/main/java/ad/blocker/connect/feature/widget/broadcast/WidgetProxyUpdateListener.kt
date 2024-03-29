package ad.blocker.connect.feature.widget.broadcast

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import ad.blocker.connect.broadcast.ProxyUpdateListener
import ad.blocker.connect.feature.widget.ToggleWidgetProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetProxyUpdateListener @Inject constructor(
    @ApplicationContext private val context: Context
) : ProxyUpdateListener {

    override fun onProxyUpdate() {
        val intent = Intent(context, ToggleWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, ToggleWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(widgetComponent)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}
