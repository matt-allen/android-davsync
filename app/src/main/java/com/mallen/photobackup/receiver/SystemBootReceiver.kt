package com.mallen.photobackup.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mallen.photobackup.helper.JobHelper

/**
 * Schedule a job to listen for new media upon system boot
 *
 * @author Matt Allen
 */
class SystemBootReceiver: BroadcastReceiver()
{
	override fun onReceive(p0: Context?, p1: Intent?)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			JobHelper.scheduleMediaJob(p0)
		}
	}
}
