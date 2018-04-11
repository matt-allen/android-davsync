package net.zekjur.davsync

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import net.zekjur.davsync.configuration.Configuration
import net.zekjur.davsync.configuration.ConfigurationFactory
import net.zekjur.davsync.model.WebDavInstance
import java.net.URL
import java.util.*

class UploadFileTask(private val context: Context, private val uri: Uri): AsyncTask<Unit, Int, Unit>()
{
	private val notificationChannel = "uploads"

	override fun doInBackground(vararg p0: Unit?)
	{
		val filename = resolveFileName(uri)
		val builder = NotificationCompat.Builder(context, notificationChannel)
		builder.setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)
		builder.setContentTitle(context.getString(R.string.uploading_to_webdav_server))
		builder.setContentText(filename)
		builder.setOngoing(true)
		builder.priority = NotificationCompat.PRIORITY_LOW
		createNotification(builder.build())

		// Upload the file
		val server = getServer()
		if (server?.isComplete() == false)
		{
			builder.setOngoing(false)
			builder.setContentTitle(context.getString(R.string.set_up_server))
			builder.setContentText(context.getString(R.string.set_up_server_message))
			return
		}
		val url = URL(Uri.withAppendedPath(Uri.parse(server?.url), filename).toString())
	}

	/**
	 * Resolve a Uri like “content://media/external/images/media/9210” to an
	 * actual filename, like “IMG_20130304_181119.jpg”
	 */
	private fun resolveFileName(uri: Uri?): String?
	{
		val projection = arrayOf(MediaStore.Images.Media.DATA)
		val cursor = context.contentResolver?.query(uri, projection, null, null, null)
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

	private fun createNotification(notification: Notification): Int
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
//			val name = context.getString(R.string.notification_channel_upload)
//			val channel = NotificationChannel(notificationChannel, name, NotificationManagerCompat.IMPORTANCE_LOW)
//			channel.description = context.getString(R.string.notification_channel_upload_description)
//			getNotificationManager().createNotificationChannel(channel)
		}
		val id = Random().nextInt()
		getNotificationManager().notify(id, notification)
		return id
	}

	private fun getNotificationManager(): NotificationManagerCompat = NotificationManagerCompat.from(context)
	private fun getServer(): WebDavInstance? = getConfiguration().getServer()
	private fun getConfiguration(): Configuration = ConfigurationFactory(context).getConfiguration()
}
