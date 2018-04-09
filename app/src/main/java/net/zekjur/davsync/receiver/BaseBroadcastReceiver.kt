package net.zekjur.davsync.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.net.ConnectivityManager
import net.zekjur.davsync.configuration.Configuration
import net.zekjur.davsync.configuration.ConfigurationFactory

/**
 * Abstracted implementation of a broadcast receiver to include some helper methods.
 *
 * @author Matt Allen
 */
abstract class BaseBroadcastReceiver: BroadcastReceiver()
{
	protected fun isOnWiFiNetwork(context: Context?): Boolean
	{
		val info = getConnectivityManager(context).activeNetworkInfo
		return info != null && ConnectivityManager.TYPE_WIFI == info.type
	}

	protected fun shouldUpload(context: Context?): Boolean = !isUploadingOnWiFiOnly(context) || isUploadingOnWiFiOnly(context) && isOnWiFiNetwork(context)
	protected fun isUploadingOnWiFiOnly(context: Context?): Boolean = getConfiguration(context).isWiFiOnly()
	protected fun getConfiguration(context: Context?): Configuration = ConfigurationFactory(context).getConfiguration()
	protected fun getConnectivityManager(context: Context?): ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
