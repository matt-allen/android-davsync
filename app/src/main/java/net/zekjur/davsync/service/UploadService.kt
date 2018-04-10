package net.zekjur.davsync.service

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import net.zekjur.davsync.model.WebDavInstance
import android.app.Notification
import android.app.NotificationChannel
import android.support.v4.app.NotificationManagerCompat
import net.zekjur.davsync.R
import net.zekjur.davsync.configuration.Configuration
import net.zekjur.davsync.configuration.ConfigurationFactory


/**
 * @author Matt Allen
 */
class UploadService: IntentService("UploadService")
{
	private val notificationChannel = "uploads"
	private val uploadNotification = 455

	override fun onHandleIntent(p0: Intent?)
	{
		// TODO
	}

	/**
	 * Resolve a Uri like “content://media/external/images/media/9210” to an
	 * actual filename, like “IMG_20130304_181119.jpg”
	 */
	private fun resolveFileName(uri: Uri): String?
	{
		val projection = arrayOf(MediaStore.Images.Media.DATA)
		val cursor = contentResolver.query(uri, projection, null, null, null)
		if (cursor == null || cursor.count == 0)
		{
			return null
		}
		val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
		cursor.moveToFirst()
		val filePathUri = Uri.parse(cursor.getString(columnIndex))
		val result = filePathUri.lastPathSegment.toString()
		cursor.close()
		return result
	}

	private fun getServer(): WebDavInstance?
	{
		return null
	}

	private fun createNotification(filename: String)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
//			val name = getString(R.string.notification_channel_upload)
//			val channel = NotificationChannel(notificationChannel, name, NotificationManagerCompat.IMPORTANCE_LOW)
//			channel.description = getString(R.string.notification_channel_upload_description)
//			getNotificationManager().createNotificationChannel(channel)
		}
		getNotificationManager().notify(uploadNotification, getUploadNotification(filename))
	}

	private fun getUploadNotification(filename: String): Notification
	{
		val builder = NotificationCompat.Builder(this, notificationChannel)
		builder.setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)
		builder.setContentTitle(getString(R.string.uploading_to_webdav_server))
		builder.setContentText(filename)
		builder.priority = NotificationCompat.PRIORITY_LOW
		return builder.build()
	}

	private fun getNotificationManager(): NotificationManagerCompat = NotificationManagerCompat.from(this)
	private fun getConfiguration(): Configuration = ConfigurationFactory(this).getConfiguration()
}
