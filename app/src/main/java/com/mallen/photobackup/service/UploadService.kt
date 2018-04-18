package com.mallen.photobackup.service

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import com.mallen.photobackup.UploadFileTask


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
			UploadFileTask(this, uri).execute()
		}
	}
}
