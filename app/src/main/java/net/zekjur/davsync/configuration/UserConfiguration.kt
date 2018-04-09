package net.zekjur.davsync.configuration

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class UserConfiguration(private val context: Context?): Configuration()
{
	override fun isUploadingVideos(): Boolean = getBoolean("upload_videos", true)
	override fun isCompressing(): Boolean = getBoolean("compress_images")
	override fun isBatteryOnly(): Boolean = getBoolean("upload_on_battery_only")
	override fun isWiFiOnly(): Boolean = getBoolean("wifi_only", true)
	override fun isUploadingImages(): Boolean = getBoolean("upload_images", true)

	private fun getPrefs(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
	private fun getBoolean(key: String, default: Boolean = false): Boolean = getPrefs().getBoolean(key, default)
}
