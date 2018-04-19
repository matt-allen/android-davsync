package com.mallen.photobackup.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import com.mallen.photobackup.R
import com.mallen.photobackup.fragment.ServerDetailsFragment
import com.mallen.photobackup.fragment.SettingsFragment
import com.mallen.photobackup.helper.JobHelper

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
		ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			JobHelper.scheduleMediaJob(this)
		}
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
