package com.touhidapps.saveit

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.util.*

/**
 * Crypto shared pref
 *
 * To make secure application
 * Use EncryptedSharedPreferences to save user name, user id, password etc.
 */
object SaveItS {

    private const val DEFAULT_SUFFIX = "_my_preferences_secured"
    private const val LENGTH = "#LENGTH_S"
    private lateinit var mPrefs: SharedPreferences
    private var isCommitEnabled = true

    private fun initPrefs(context: Context, prefsName: String, isCommitEnabled: Boolean) {

        mPrefs = EncryptedSharedPreferences.create(
            prefsName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        SaveItS.isCommitEnabled = isCommitEnabled

    }

    private val preferences: SharedPreferences
        get() {
            if (mPrefs != null) {
                return mPrefs
            }
            throw RuntimeException(
                "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate."
            )
        }

    val all: Map<String, *>
        get() = preferences.all

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun getDouble(key: String, defValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            preferences.getLong(
                key,
                java.lang.Double.doubleToLongBits(defValue)
            )
        )
    }

    fun getFloat(key: String, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String): String {
        val v: String? = preferences.getString(key, defValue)
        v?.let {
            return it
        }
        return defValue
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(
        key: String,
        defValue: Set<String?>?
    ): Set<String?>? {
        val prefs = preferences
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            prefs!!.getStringSet(key, defValue)
        } else { // Workaround for pre-HC's missing getStringSet
            getOrderedStringSet(key, defValue)
        }
    }

    fun getOrderedStringSet(
        key: String,
        defValue: Set<String?>?
    ): Set<String?>? {
        val prefs = preferences
        if (prefs!!.contains(key + LENGTH)) {
            val set =
                LinkedHashSet<String?>()
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                for (i in 0 until stringSetLength) {
                    set.add(prefs.getString("$key[$i]", null))
                }
            }
            return set
        }
        return defValue
    }

    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun putInt(key: String?, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun putDouble(key: String, value: Double) {
        val editor = preferences.edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun putFloat(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun putString(key: String, value: String?) {
        val editor = preferences.edit()
        editor.putString(key, value)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(
        key: String,
        value: Set<String?>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val editor = preferences.edit()
            editor.putStringSet(key, value)
            if (isCommitEnabled) {
                editor.commit()
            } else {
                editor.apply()
            }
        } else { // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value)
        }
    }

    fun putOrderedStringSet(
        key: String,
        value: Set<String?>
    ) {
        val editor = preferences.edit()
        var stringSetLength = 0
        if (mPrefs.contains(key + LENGTH)) { // First read what the value was
            stringSetLength = mPrefs.getInt(key + LENGTH, -1)
        }
        editor.putInt(key + LENGTH, value.size)
        var i = 0
        for (aValue in value) {
            editor.putString("$key[$i]", aValue)
            i++
        }
        while (i < stringSetLength) {
            // Remove any remaining values
            editor.remove("$key[$i]")
            i++
        }
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    fun remove(key: String) {
        val prefs = preferences
        val editor = prefs.edit()
        if (prefs.contains(key + LENGTH)) { // Workaround for pre-HC's lack of StringSets
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH)
                for (i in 0 until stringSetLength) {
                    editor.remove("$key[$i]")
                }
            }
        }
        editor.remove(key)
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    fun clear(): SharedPreferences.Editor {
        val editor = preferences.edit().clear()
        if (isCommitEnabled) {
            editor.commit()
        } else {
            editor.apply()
        }
        return editor
    }

    fun edit(): SharedPreferences.Editor {
        return preferences.edit()
    }

    class Builder {

        private lateinit var mKey: String
        private var isCommitEnabled =
            true // commit is force and can return value, apply is async
        private lateinit var mContext: Context
        private var mUseDefault = false

        fun setPrefsName(prefsName: String): Builder {
            mKey = prefsName
            return this
        }

        fun setContext(context: Context): Builder {
            mContext = context
            return this
        }

        fun setIsCommitEnabled(isCommitEnabled: Boolean): Builder {
            this.isCommitEnabled = isCommitEnabled
            return this
        }

        fun setUseDefaultSharedPreference(defaultSharedPreference: Boolean): Builder {
            mUseDefault = defaultSharedPreference
            return this
        }

        fun build() {
            if (mContext == null) {
                println("Please ensure initialisation in Application class")
                throw RuntimeException("Context not set, please set context before building the Prefs instance.")
            }
            if (TextUtils.isEmpty(mKey)) {
                mKey = mContext.packageName
            }
            if (mUseDefault) {
                mKey += DEFAULT_SUFFIX
            }

            initPrefs(mContext, mKey, isCommitEnabled)
        }
    }

}