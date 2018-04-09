package net.zekjur.davsync.configuration

import android.content.Context

/**
 * Factory-style class for creating a Configuration object.
 *
 * This is a builder for the purposes of mocking the
 * return in certain scenarios (In the future).
 */
class ConfigurationFactory(private val context: Context?)
{
	fun getConfiguration(): Configuration = UserConfiguration(context)
}
