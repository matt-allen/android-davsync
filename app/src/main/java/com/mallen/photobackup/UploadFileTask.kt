package com.mallen.photobackup

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Base64.*
import android.util.Log
import com.mallen.photobackup.configuration.Configuration
import com.mallen.photobackup.configuration.ConfigurationFactory
import com.mallen.photobackup.model.WebDavInstance
import java.util.*
import okhttp3.*


class UploadFileTask(private val context: Context, private val uri: Uri): AsyncTask<Unit, Int, Unit>(), UploadProgressListener
{
	private val tag = UploadFileTask::class.java.simpleName

	private val notificationChannel = "uploads"
	private val progressNotification = NotificationCompat.Builder(context, notificationChannel)
	private var progressNotificationId = 0

	override fun doInBackground(vararg p0: Unit?)
	{
		Log.d(tag, "Started upload task for media file")
		Log.d(tag, "Media file is located at: $uri")
		val filename = resolveFileName(uri)

		progressNotification.setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)
		progressNotification.setContentTitle(context.getString(R.string.uploading_to_webdav_server))
		progressNotification.setContentText(filename)
		progressNotification.setOngoing(true)
		progressNotification.setProgress(100, 0, false)
		progressNotification.priority = NotificationCompat.PRIORITY_LOW
		updateNotification()

		// Upload the file
		val server = getServer()
		if (server?.isComplete() == false)
		{
			progressNotification.setOngoing(false)
			progressNotification.setContentTitle(context.getString(R.string.set_up_server))
			progressNotification.setContentText(context.getString(R.string.set_up_server_message))
			return
		}
		val url = Uri.withAppendedPath(Uri.parse(server?.url), filename).toString()
		val authHeader = "Basic " + encodeToString("${server?.username}:${server?.password}".toByteArray(), NO_WRAP)
		val httpClient = OkHttpClient()
		val body = ProgressRequestBody(RequestBody.create(MediaType.parse(resolveMimeType(uri)), context.contentResolver.openInputStream(uri).buffered().use { it.readBytes() }), this)
		val request = Request.Builder()
				.header("Authorization", authHeader)
				.url(url)
				.put(body)
				.build()
		val response = httpClient.newCall(request).execute()
		getNotificationManager().cancel(notificationChannel, progressNotificationId)

		if (!response.isSuccessful)
		{
			val endNotification = NotificationCompat.Builder(context, notificationChannel)
			endNotification.setContentTitle(context.getString(R.string.upload_failed))
			endNotification.setOngoing(false)
			if (response.code() == 401)
			{
				endNotification.setContentText(context.getString(R.string.incorrect_username_password))
			}
			getNotificationManager().notify(notificationChannel, Random().nextInt(), endNotification.build())
		}
	}

	override fun onProgressChanged(progress: Int)
	{
		progressNotification.setProgress(100, progress, false)
		updateNotification()
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

	private fun updateNotification()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			progressNotificationId = createNotification(progressNotification.build())
		}
		else
		{
			progressNotificationId = createNotificationCompat(progressNotification.build())
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun createNotification(notification: Notification): Int
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			val name = context.getString(R.string.notification_channel_upload)
			val channel = NotificationChannel(notificationChannel, name, NotificationManager.IMPORTANCE_LOW)
			channel.description = context.getString(R.string.notification_channel_upload_description)
			(context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(channel)
		}
		return createNotificationCompat(notification)
	}

	private fun createNotificationCompat(notification: Notification): Int
	{
		if (progressNotificationId == 0)
		{
			progressNotificationId = Random().nextInt()
		}
		getNotificationManager().notify(notificationChannel, progressNotificationId, notification)
		return progressNotificationId
	}

	private fun resolveMimeType(uri: Uri): String = context.contentResolver.getType(uri)
	private fun getNotificationManager(): NotificationManagerCompat = NotificationManagerCompat.from(context)
	private fun getServer(): WebDavInstance? = getConfiguration().getServer()
	private fun getConfiguration(): Configuration = ConfigurationFactory(context).getConfiguration()
}
