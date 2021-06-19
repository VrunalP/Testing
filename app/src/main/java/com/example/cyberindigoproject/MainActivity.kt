package com.example.cyberindigoproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.cyberindigoproject.HomeScreen.HomeScreenActivity
import com.example.cyberindigoproject.LoginScreen.LoginScreenActivity

class MainActivity : AppCompatActivity() {

    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToMainScreen()
    }

    private fun navigateToMainScreen() {
        Handler().postDelayed({

            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)

            val sharedNameValue = sharedPreferences.getString("token","defaultname")

            Log.d("TAG", "onResponse: "+sharedNameValue);

            if (sharedNameValue.equals("defaultname"))
            {
                val nextScreenIntent = Intent(this, LoginScreenActivity::class.java)
                startActivity(nextScreenIntent)
                finish()
            }else
            {
                val nextScreenIntent = Intent(this, HomeScreenActivity::class.java)
                startActivity(nextScreenIntent)
                finish()
            }

        }, SPLASH_DELAY.toLong())
    }
}