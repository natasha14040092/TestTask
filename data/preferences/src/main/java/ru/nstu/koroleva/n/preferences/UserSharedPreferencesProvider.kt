package ru.nstu.koroleva.n.preferences

import android.content.Context
import ru.nstu.koroleva.n.preferences.model.UserModel

class UserSharedPreferencesProvider(context: Context) {
    companion object {
        private lateinit var INSTANCE: UserSharedPreferencesProvider

        @JvmStatic
        fun getInstance(context: Context): UserSharedPreferencesProvider {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = UserSharedPreferencesProvider(context)
            }
            return INSTANCE
        }

        private const val USER_PREFERENCES = "user_preferences"
        private const val SIGN_UP = "sign_up"
        private const val NAME = "name"
        private const val SURNAME = "surname"
        private const val BIRTHDATE = "birthdate"
        private const val PASSWORD = "password"
        private const val EMPTY_TEXT = ""
    }

    private val sharedPreferences =
        context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)

////TODO
//    init {
//        sharedPreferences.edit().clear().apply()
//    }

    fun putUserInfo(userModel: UserModel) {
        sharedPreferences.edit()
            .putString(NAME, userModel.name)
            .putString(SURNAME, userModel.surname)
            .putString(BIRTHDATE, userModel.birthdate)
            .putString(PASSWORD, userModel.password)
            .putBoolean(SIGN_UP, true)
            .apply()
    }

    fun getUserInfo(): UserModel {
        return UserModel(
            sharedPreferences.getString(NAME, EMPTY_TEXT) as String,
            sharedPreferences.getString(SURNAME, EMPTY_TEXT) as String,
            sharedPreferences.getString(BIRTHDATE, EMPTY_TEXT) as String,
            sharedPreferences.getString(PASSWORD, EMPTY_TEXT) as String,
        )
    }

    fun getSignUpInfo(): Boolean {
        return sharedPreferences.getBoolean(SIGN_UP, false)
    }
}