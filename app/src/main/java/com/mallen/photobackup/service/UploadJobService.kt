package com.mallen.photobackup.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import com.mallen.photobackup.configuration.Configuration
import com.mallen.photobackup.configuration.ConfigurationFactory
import com.mallen.photobackup.helper.UploadHelper

/**
 * @author Matt Allen
 */
class UploadJobService: JobService()
{
	override fun onStartJob(job: JobParameters?): Boolean
	{
		if (Build.VERSION.SDK_INT >= 24)
		{
			if (job?.triggeredContentUris?.isNotEmpty() == true)
			{
				job.triggeredContentUris.forEach { UploadHelper.uploadImage(this, it) }
			}
		}
		jobFinished(job, true)
		return true
	}

	override fun onStopJob(job: JobParameters?): Boolean = true

	private fun getConfiguration(context: Context? = this): Configuration = ConfigurationFactory(context).getConfiguration()
}
