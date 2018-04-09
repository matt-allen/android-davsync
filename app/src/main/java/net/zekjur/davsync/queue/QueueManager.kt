package net.zekjur.davsync.queue

import net.zekjur.davsync.model.QueuedTask

/**
 * The previous implementation of this used SQLite to provide persistence to the application
 * but that is going to be too slow for the use case so it now persists the queue to disk
 * in the form of a JSON file. This will load much faster and be very light-weight.
 *
 * @author Matt Allen
 */
object QueueManager
{
	fun addToQueue(task: QueuedTask)
	{
		// TODO Implement
	}

	fun loadQueue(): List<QueuedTask>
	{
		return emptyList() // TODO Implement
	}
}
