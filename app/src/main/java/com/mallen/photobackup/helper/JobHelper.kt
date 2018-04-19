package com.mallen.photobackup.helper

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.mallen.photobackup.service.UploadJobService

/**
 * The JobScheduler API is garbage compared the old way of using Intent and BroadcastReceiver.
 * I've made this helper to make the API more accessible.
 *
 * @author Matt Allen
 */
object JobHelper
{
	fun scheduleNetworkJob(context: Context?)
	{
		val scheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
		val job = JobInfo.Builder(0, ComponentName(context, UploadJobService::class.java))
				.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
				.build()
		scheduler?.schedule(job)
	}

	@TargetApi(Build.VERSION_CODES.O)
	fun scheduleMediaJob(context: Context?)
	{
		val scheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
		val job = JobInfo.Builder(0, ComponentName(context, UploadJobService::class.java))
				.addTriggerContentUri(JobInfo.TriggerContentUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
				.addTriggerContentUri(JobInfo.TriggerContentUri(MediaStore.Images.Media.INTERNAL_CONTENT_URI, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
				.build()
		scheduler?.schedule(job)
	}
}
