package net.zekjur.davsync.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import net.zekjur.davsync.UploadService

/**
 * This Activity responds to the Android share intent and posts each of
 * the media files into the uploading service.
 */
class ShareActivity : Activity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		finish()

		val intent = intent
		val action = intent.action
		val type = intent.type

		if (type == null || !intent.hasExtra(Intent.EXTRA_STREAM))
		{
			return
		}

		if (Intent.ACTION_SEND == action)
		{
			val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
			shareImageWithUri(imageUri)
		}
		else if (Intent.ACTION_SEND_MULTIPLE == action)
		{
			val list = intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)
			for (p in list)
			{
				shareImageWithUri(p as Uri)
			}
		}
	}

	private fun shareImageWithUri(uri: Uri)
	{
		val intent = Intent(this, UploadService::class.java)
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		startService(intent)
	}
}
