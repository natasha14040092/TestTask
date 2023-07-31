package ru.nstu.koroleva.n.preferences

import android.content.Context

class UserSharedPreferencesProvider(context: Context) {
    companion object {
        private lateinit var INSTANCE: UserSharedPreferencesProvider

        @JvmStatic
        fun getInstance(context: Context): UserSharedPreferencesProvider{
            if (!::INSTANCE.isInitialized) {
                INSTANCE = UserSharedPreferencesProvider(context)
            }
            return INSTANCE
        }

        private const val USER_PREFERENCES = "user_preferences"
        private const val SIGN_UP = "sign_up"
        private const val USER = "user "
    }

    private val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)

    fun putUserInfo(userInfoSet: Set<String>) {
        sharedPreferences.edit().putStringSet(USER, userInfoSet).apply()

    }

    fun putSignUpInfo(value: Boolean) {
        sharedPreferences.edit().putBoolean(SIGN_UP, value).apply()
    }

    fun getUserInfo(): Set<String> {
        return sharedPreferences.getStringSet(USER, HashSet()) as Set<String>
    }

    fun getSignUpInfo() : Boolean {
        return sharedPreferences.getBoolean(SIGN_UP, false)
    }

}