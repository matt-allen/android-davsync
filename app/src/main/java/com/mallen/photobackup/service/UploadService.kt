package com.mallen.photobackup.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import com.mallen.photobackup.configuration.Configuration
import com.mallen.photobackup.configuration.ConfigurationFactory
import com.mallen.photobackup.helper.JobHelper
import com.mallen.photobackup.helper.UploadHelper
import com.mallen.photobackup.queue.QueueManager


/**
 * @author Matt Allen
 */
class UploadService: IntentService("UploadService")
{
	override fun onCreate()
	{
		super.onCreate()
		val config = getConfiguration()
		if (!config.isWiFiOnly() || (config.isWiFiOnly() && !isMeteredNetwork()))
		{
			val queue = QueueManager.loadQueue(this)
			if (queue.isNotEmpty())
			{
				queue.forEach { UploadHelper.uploadImage(this, it.uri) }
			}
			QueueManager.emptyQueue(this)
		}
		JobHelper.scheduleNetworkJob(this)
	}

	override fun onHandleIntent(intent: Intent?)
	{
		val uri = intent?.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
		if (uri != null)
		{
			UploadHelper.uploadImage(this, uri)
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			JobHelper.scheduleMediaJob(this)
		}
	}

	private fun isMeteredNetwork(): Boolean = getConnectivityManager().isActiveNetworkMetered
	private fun getConfiguration(context: Context? = this): Configuration = ConfigurationFactory(context).getConfiguration()
	private fun getConnectivityManager(context: Context? = this): ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
