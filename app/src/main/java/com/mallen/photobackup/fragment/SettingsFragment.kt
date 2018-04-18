package com.mallen.photobackup.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.mallen.photobackup.R

class SettingsFragment: PreferenceFragmentCompat()
{
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
	{
		addPreferencesFromResource(R.xml.pref_upload)
	}
}
