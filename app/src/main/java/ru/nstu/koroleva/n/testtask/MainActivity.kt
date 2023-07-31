package ru.nstu.koroleva.n.testtask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.findNavController
import ru.nstu.koroleva.n.navigation.HOME_URI
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userSharedPreferencesProvider = UserSharedPreferencesProvider.getInstance(this)
        if (userSharedPreferencesProvider.getSignUpInfo()) {
            Toast.makeText(this, getString(R.string.sign_in_success), Toast.LENGTH_SHORT).show()
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(HOME_URI.toUri())
        }
    }
}