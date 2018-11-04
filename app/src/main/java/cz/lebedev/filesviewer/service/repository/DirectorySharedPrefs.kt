package cz.lebedev.filesviewer.service.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.preference.PreferenceManager
import cz.lebedev.filesviewer.Env
import cz.lebedev.filesviewer.R

class DirectorySharedPrefs(val ctx:Context){

    var sharedPreferences: SharedPreferences

    init{
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun getDefaultFolder(): String? {
        return sharedPreferences.getString(ctx.getString(R.string.prefsValue), Env.getRootDir())
    }


}