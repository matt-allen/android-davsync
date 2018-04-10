package net.zekjur.davsync.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_server_configuration.*
import net.zekjur.davsync.R
import net.zekjur.davsync.configuration.Configuration
import net.zekjur.davsync.configuration.ConfigurationFactory
import net.zekjur.davsync.isIpv4
import net.zekjur.davsync.isUrl
import net.zekjur.davsync.model.WebDavInstance

class ServerDetailsFragment: Fragment()
{
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		return inflater.inflate(R.layout.fragment_server_configuration, container, false)
	}

	override fun onResume()
	{
		super.onResume()
		val config = getConfiguration().getServer()
		url?.setText(config?.url ?: "")
		username?.setText(config?.username ?: "")
		password?.setText(config?.password ?: "")
		save?.setOnClickListener { updateCredentials() }
	}

	private fun updateCredentials()
	{
		if (validateInput())
		{
			val url = url?.text.toString()
			val username = username?.text.toString()
			val password = password?.text.toString()
			getConfiguration().setServer(WebDavInstance(url, username, password))
			Toast.makeText(context, R.string.updated_server_settings, Toast.LENGTH_SHORT).show()
		}
	}

	private fun validateInput(): Boolean
	{
		listOf(url_wrapper, username_wrapper, password_wrapper).forEach {
			it?.error = null
			it?.isErrorEnabled = false
		}
		if (!url?.text.toString().isUrl() && !url?.text.toString().isIpv4())
		{
			url_wrapper?.error = getString(R.string.enter_valid_url)
			url_wrapper?.isErrorEnabled = true
			return false
		}
		if (username?.text.toString().isBlank())
		{
			username_wrapper?.error = getString(R.string.enter_valid_username)
			username_wrapper?.isErrorEnabled = true
			return false
		}
		if (password?.text.toString().isBlank())
		{
			password_wrapper?.error = getString(R.string.enter_valid_password)
			password_wrapper?.isErrorEnabled = true
			return false
		}
		return true
	}

	private fun getConfiguration(): Configuration = ConfigurationFactory(context).getConfiguration()
}
