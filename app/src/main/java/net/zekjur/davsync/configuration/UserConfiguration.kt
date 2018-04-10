package net.zekjur.davsync.configuration

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.moldedbits.r2d2.R2d2
import net.zekjur.davsync.model.WebDavInstance

class UserConfiguration(private val context: Context?): Configuration()
{
	private val serverUrl = "server_url"
	private val serverUsername = "server_username"
	private val serverPassword = "server_password"

	override fun getServer(): WebDavInstance
	{
		val r2d2 = R2d2(context)
		val url = r2d2.decryptData(getString(serverUrl) ?: "")
		val user = r2d2.decryptData(getString(serverUsername) ?: "")
		val password = r2d2.decryptData(getString(serverPassword) ?: "")
		return WebDavInstance(url, user, password)
	}

	override fun setServer(server: WebDavInstance)
	{
		val r2d2 = R2d2(context)
		setString(serverUrl, r2d2.encryptData(server.url))
		setString(serverUsername, r2d2.encryptData(server.username))
		setString(serverPassword, r2d2.encryptData(server.password))
	}

	override fun isUploadingVideos(): Boolean = getBoolean("upload_videos", true)
	override fun isCompressing(): Boolean = getBoolean("compress_images")
	override fun isBatteryOnly(): Boolean = getBoolean("upload_on_battery_only")
	override fun isWiFiOnly(): Boolean = getBoolean("wifi_only", true)
	override fun isUploadingImages(): Boolean = getBoolean("upload_images", true)

	private fun getPrefs(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
	private fun getBoolean(key: String, default: Boolean = false): Boolean = getPrefs().getBoolean(key, default)
	private fun getString(key: String, default: String? = null): String? = getPrefs().getString(key, default)
	private fun setString(key: String, value: String?) = getPrefs().edit().putString(key, value).apply()
}
