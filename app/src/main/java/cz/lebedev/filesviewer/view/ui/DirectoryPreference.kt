package cz.lebedev.filesviewer.view.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import cz.lebedev.filesviewer.Env
import cz.lebedev.filesviewer.R
import dagger.android.support.AndroidSupportInjection


class DirectoryPreference : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    private lateinit var etp: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.directory_preference,rootKey)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }



    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val key = getText(R.string.prefsValue).toString()
        etp = findPreference(key) as ListPreference
        etp.setSummary(preferenceScreen.sharedPreferences.getString(key, Env.getRootDir()))
        val actuallDir = arguments?.getString(TAG, "") as CharSequence
        var arr = Array<CharSequence>(1,{actuallDir})
        etp.entries = arr
        etp.entryValues = arr
    }


    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        etp.setSummary(sharedPreferences?.getString(key, Env.getRootDir()))
    }

    companion object {
        val TAG : String = "DirectoryPreference"
    }
}


