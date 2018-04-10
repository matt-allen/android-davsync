package net.zekjur.davsync.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import net.zekjur.davsync.R
import net.zekjur.davsync.fragment.ServerDetailsFragment
import net.zekjur.davsync.fragment.SettingsFragment

/**
 * Entry point of the application where the user can control
 * their settings, server config etc.
 *
 * @author Matt Allen
 */
class MainActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		bottom_bar?.setOnNavigationItemSelectedListener(this)
		setFragment(ServerDetailsFragment())
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_server -> setFragment(ServerDetailsFragment())
			R.id.action_settings -> setFragment(SettingsFragment())
		}
		return true
	}

	private fun setFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
}
