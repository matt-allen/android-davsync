package net.zekjur.davsync.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import net.zekjur.davsync.DavSyncOpenHelper
import net.zekjur.davsync.UploadService

class NewMediaReceiver : BaseBroadcastReceiver()
{
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
			Log.d("davsync", "automatic camera picture sync is disabled, ignoring")
			return
		}

		if (isNewVid && !getConfiguration(context).isUploadingVideos())
		{
			Log.d("davsync", "automatic camera video sync is disabled, ignoring")
			return
		}

		val uri = intent.data
		if (shouldUpload(context))
		{
			Log.d("davsync", "Trying to upload $uri immediately (on WIFI)")
			val ulIntent = Intent(context, UploadService::class.java)
			ulIntent.putExtra(Intent.EXTRA_STREAM, uri)
			context.startService(ulIntent)
		}
		else
		{
			Log.d("davsync", "Queueing " + uri + "for later (not on WIFI)")
			// otherwise, queue the image for later
			val helper = DavSyncOpenHelper(context)
			helper.queueUri(uri)
		}
	}
}
