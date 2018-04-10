package net.zekjur.davsync.configuration

import net.zekjur.davsync.model.WebDavInstance

abstract class Configuration
{
	abstract fun isUploadingImages(): Boolean
	abstract fun isUploadingVideos(): Boolean
	abstract fun isCompressing(): Boolean
	abstract fun isBatteryOnly(): Boolean
	abstract fun isWiFiOnly(): Boolean
	abstract fun getServer(): WebDavInstance?
	abstract fun setServer(server: WebDavInstance)
}
