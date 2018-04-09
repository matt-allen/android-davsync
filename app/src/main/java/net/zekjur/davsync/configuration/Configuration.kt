package net.zekjur.davsync.configuration

abstract class Configuration
{
	abstract fun isUploadingImages(): Boolean
	abstract fun isUploadingVideos(): Boolean
	abstract fun isCompressing(): Boolean
	abstract fun isBatteryOnly(): Boolean
	abstract fun isWiFiOnly(): Boolean
}
