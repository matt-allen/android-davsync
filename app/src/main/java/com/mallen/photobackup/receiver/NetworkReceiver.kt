package com.mallen.photobackup.receiver

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import com.mallen.photobackup.queue.QueueManager
import com.mallen.photobackup.service.UploadService

class NetworkReceiver : BaseBroadcastReceiver()
{
	override fun onReceive(context: Context, intent: Intent)
	{
		if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action && shouldUpload(context))
		{
			// Upload the queue of parked image/video requests
			val uris = QueueManager.loadQueue(context)
			for (uri in uris)
			{
				val uploadIntent = Intent(context, UploadService::class.java)
				uploadIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri.uri.toString()))
				context.startService(uploadIntent)
			}
			QueueManager.emptyQueue(context)
		}
	}
}
