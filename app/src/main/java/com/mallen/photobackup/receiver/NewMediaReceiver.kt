package com.mallen.photobackup.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import com.mallen.photobackup.model.QueuedTask
import com.mallen.photobackup.queue.QueueManager
import com.mallen.photobackup.service.UploadService

class NewMediaReceiver : BaseBroadcastReceiver()
{
	private val tag = NewMediaReceiver::class.java.simpleName

	override fun onReceive(context: Context, intent: Intent)
	{
		val isNewPic = android.hardware.Camera.ACTION_NEW_PICTURE == intent.action
		val isNewVid = android.hardware.Camera.ACTION_NEW_VIDEO == intent.action

		if (!isNewPic && !isNewVid)
		{
			return
		}

		if (isNewPic && !getConfiguration(context).isUploadingImages())
		{
			Log.d(tag, "User does not want images uploaded")
			return
		}

		if (isNewVid && !getConfiguration(context).isUploadingVideos())
		{
			Log.d(tag, "User does not want videos uploaded")
			return
		}

		val uri = intent.data
		if (shouldUpload(context))
		{
			Log.d(tag, "Trying to upload $uri immediately (on WiFi)")
			val ulIntent = Intent(context, UploadService::class.java)
			ulIntent.putExtra(Intent.EXTRA_STREAM, uri)
			context.startService(ulIntent)
		}
		else
		{
			Log.d(tag, "Queueing $uri for later (not on WiFi)")
			// otherwise, queue the image for later
			QueueManager.addToQueue(context, QueuedTask(uri))
		}
	}
}
