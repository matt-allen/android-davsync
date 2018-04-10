package net.zekjur.davsync.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_server_configuration.*
import net.zekjur.davsync.R
import net.zekjur.davsync.configuration.Configuration
import net.zekjur.davsync.configuration.ConfigurationFactory

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
	}

	private fun updateCredentials()
	{

	}

	private fun getConfiguration(): Configuration = ConfigurationFactory(context).getConfiguration()
}
