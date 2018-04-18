package com.mallen.photobackup.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mallen.photobackup.configuration.Configuration
import com.mallen.photobackup.configuration.ConfigurationFactory
import com.mallen.photobackup.service.UploadService

class NewMediaReceiver : BroadcastReceiver()
{
	override fun onReceive(context: Context, intent: Intent)
	{
		val isNewPic = android.hardware.Camera.ACTION_NEW_PICTURE == intent.action
		val isNewVid = android.hardware.Camera.ACTION_NEW_VIDEO == intent.action

		val uri = intent.data
		if ((isNewPic && getConfiguration(context).isUploadingImages()) || (isNewVid && getConfiguration(context).isUploadingVideos()))
		{
			val ulIntent = Intent(context, UploadService::class.java)
			ulIntent.putExtra(Intent.EXTRA_STREAM, uri)
			context.startService(ulIntent)
		}
	}

	private fun getConfiguration(context: Context?): Configuration = ConfigurationFactory(context).getConfiguration()
}
