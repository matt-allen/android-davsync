package net.zekjur.davsync.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import net.zekjur.davsync.R

class SettingsFragment: PreferenceFragmentCompat()
{
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
	{
		addPreferencesFromResource(R.xml.pref_upload)
	}
}
