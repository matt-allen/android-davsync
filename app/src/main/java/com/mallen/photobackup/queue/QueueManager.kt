package com.mallen.photobackup.queue

import android.content.Context
import com.google.gson.Gson
import com.mallen.photobackup.model.QueuedTask
import java.io.File

/**
 * The previous implementation of this used SQLite to provide persistence to the application
 * but that is going to be too slow for the use case so it now persists the queue to disk
 * in the form of a JSON file. This will load much faster and be very light-weight.
 *
 * @author Matt Allen
 */
object QueueManager
{
	private const val fileName = "queued_uploads.json"

	fun addToQueue(context: Context?, task: QueuedTask)
	{
		val file = loadFile(context)
		val cache = Gson().fromJson(file, QueueCache::class.java)
		cache.tasks.add(task)
		saveFile(context, Gson().toJson(cache))
	}

	fun loadQueue(context: Context?): List<QueuedTask>
	{
		val file = loadFile(context)
		val cache = Gson().fromJson(file, QueueCache::class.java)
		return cache.tasks
	}

	fun emptyQueue(context: Context?)
	{
		File(createAbsoluteFilePath(context)).delete()
	}

	private fun loadFile(context: Context?): String
	{
		return ""
	}

	private fun saveFile(context: Context?, contents: String?)
	{

	}

	private fun createAbsoluteFilePath(context: Context?): String = File(context?.filesDir, "cache/${fileName}").absolutePath

	private data class QueueCache(val tasks: ArrayList<QueuedTask>)
}
