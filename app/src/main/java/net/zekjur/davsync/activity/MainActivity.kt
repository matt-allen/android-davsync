package net.zekjur.davsync.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.zekjur.davsync.R

/**
 * Entry point of the application where the user can control
 * their settings, server config etc.
 *
 * @author Matt Allen
 */
class MainActivity: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
}
