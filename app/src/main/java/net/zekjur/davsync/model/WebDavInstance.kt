package net.zekjur.davsync.model

import net.zekjur.davsync.isUrl

/**
 * @author Matt Allen
 */
data class WebDavInstance(val url: String, val username: String, val password: String)
{
	fun isComplete(): Boolean = url.isUrl() && username.isNotBlank() && password.isNotBlank()
}
