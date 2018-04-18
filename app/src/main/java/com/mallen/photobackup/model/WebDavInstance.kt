package com.mallen.photobackup.model

import com.mallen.photobackup.isIpv4
import com.mallen.photobackup.isUrl

/**
 * @author Matt Allen
 */
data class WebDavInstance(val url: String, val username: String, val password: String)
{
	fun isComplete(): Boolean = (url.isUrl() || url.isIpv4()) && username.isNotBlank() && password.isNotBlank()
}
