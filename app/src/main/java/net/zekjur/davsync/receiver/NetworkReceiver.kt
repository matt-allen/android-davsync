package net.zekjur.davsync.receiver

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import net.zekjur.davsync.DavSyncOpenHelper
import net.zekjur.davsync.UploadService

class NetworkReceiver : BaseBroadcastReceiver()
{
	override fun onReceive(context: Context, intent: Intent)
	{
		if (ConnectivityManager.CONNECTIVITY_ACTION != intent.action)
		{
			return
		}

		if (shouldUpload(context))
		{
			// Upload the queue of parked image/video requests
			val helper = DavSyncOpenHelper(context)
			val uris = helper.queuedUris
			for (uri in uris)
			{
				val uploadIntent = Intent(context, UploadService::class.java)
				uploadIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
				context.startService(uploadIntent)
			}
		}
	}
}
