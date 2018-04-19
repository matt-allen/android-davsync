package com.mallen.photobackup.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import com.mallen.photobackup.UploadFileTask
import com.mallen.photobackup.configuration.Configuration
import com.mallen.photobackup.configuration.ConfigurationFactory
import com.mallen.photobackup.model.QueuedTask
import com.mallen.photobackup.queue.QueueManager

/**
 * @author Matt Allen
 */
object UploadHelper
{
	fun uploadImage(context: Context?, uri: Uri)
	{
		if (isMeteredNetwork(context) && getConfiguration(context).isWiFiOnly())
		{
			QueueManager.addToQueue(context, QueuedTask(uri))
			JobHelper.scheduleNetworkJob(context)
		}
		else
		{
			UploadFileTask(context!!, uri).execute()
		}
	}

	fun isMeteredNetwork(context: Context?): Boolean = getConnectivityManager(context).isActiveNetworkMetered
	private fun getConnectivityManager(context: Context?): ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	private fun getConfiguration(context: Context?): Configuration = ConfigurationFactory(context).getConfiguration()
}
