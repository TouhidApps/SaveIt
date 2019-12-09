package com.touhidapps.saveitExample

import android.app.Application
import android.content.ContextWrapper
import com.touhidapps.saveit.SaveIt
import com.touhidapps.saveit.SaveItS

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * For general data
         */
        SaveIt.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName + "_new_name")
            .setIsCommitEnabled(true) // commit enable true means save instantly, false means save asynchronously
            .setUseDefaultSharedPreference(false) // if false use setPrefsName() method to set name
            .build()


        /**
         * For Secured data like userName, password, token, email etc
         */
        SaveItS.Builder()
            .setContext(this)
            .setPrefsName(packageName + "_new_name_secured")
            .setIsCommitEnabled(true) // commit enable true means save instantly, false means save asynchronously
            .setUseDefaultSharedPreference(false) // if false use setPrefsName() method to set name
            .build()

    }

}