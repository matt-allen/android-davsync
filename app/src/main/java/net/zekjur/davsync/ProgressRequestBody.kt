package net.zekjur.davsync

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

/**
 * For the notification displayed when the file is uploading, seeing the progress would be nice.
 * This will report the upload progress of the upload of the request body sent via OkHttp3.
 *
 * Mostly taken from https://stackoverflow.com/questions/35528751/okhttp-3-tracking-multipart-upload-progress
 *
 * @author Matt Allen
 */
class ProgressRequestBody(private val body: RequestBody, private val listener: UploadProgressListener): RequestBody()
{
	private var sink: CountingSink? = null

	override fun writeTo(sink: BufferedSink?)
	{
		this.sink = CountingSink(sink!!, contentLength(), listener)
		val buffer = Okio.buffer(this.sink!!)
		body.writeTo(buffer)
		buffer.flush()
	}

	override fun contentLength(): Long
	{
		return try
		{
			body.contentLength()
		}
		catch (e: Exception)
		{
			-1
		}
	}

	override fun contentType(): MediaType? = body.contentType()
}

private class CountingSink(sink: Sink, private val contentLength: Long, private val listener: UploadProgressListener): ForwardingSink(sink)
{
	private var bytesWritten: Long = 0

	override fun write(source: Buffer?, byteCount: Long)
	{
		super.write(source!!, byteCount)
		bytesWritten += byteCount
		listener.onProgressChanged((100f * bytesWritten / contentLength).toInt())
	}
}

interface UploadProgressListener
{
	fun onProgressChanged(progress: Int)
}
