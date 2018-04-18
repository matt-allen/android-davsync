package com.mallen.photobackup.service

import android.app.IntentService
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
class UploadService: IntentService("UploadService")
{
	override fun onHandleIntent(intent: Intent?)
	{
		val uri = intent?.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
		if (uri != null)
		{
			if (isMeteredNetwork() && getConfiguration().isWiFiOnly())
			{
				QueueManager.addToQueue(this, QueuedTask(uri))
				schedulePendingUploads()
			}
			else
			{
				UploadFileTask(this, uri).execute()
			}
		}
	}

	/**
	 * If we're on a metered network and the user doesn't want to use up their allowance,
	 * schedule this to happen when we're on WiFi/Ethernet etc.
	 */
	private fun schedulePendingUploads()
	{
		val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
		val job = JobInfo.Builder(0, ComponentName(this, UploadService::class.java))
				.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
				.setRequiresCharging(true)
				.build()
		scheduler?.schedule(job)
	}

	private fun isMeteredNetwork(): Boolean = getConnectivityManager().isActiveNetworkMetered
	private fun getConfiguration(context: Context? = this): Configuration = ConfigurationFactory(context).getConfiguration()
	private fun getConnectivityManager(context: Context? = this): ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
