package com.example.cyberindigoproject.LoginScreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.cyberindigoproject.HomeScreen.HomeScreenActivity
import com.example.cyberindigoproject.R
import kotlinx.android.synthetic.main.activity_login_screen.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException

class LoginScreenActivity : AppCompatActivity() {

    private val sharedPrefFile = "kotlinsharedpreference"

    var dialog : Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_loading)



       // val login_button = findViewById(R.id.login_button) as Button
        login_button.setOnClickListener {
            dialog!!.show()

            // Toast.makeText(this@LoginScreenActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            doLogin()
        }
    }


    fun doLogin()
    {

        val okHttpClient = OkHttpClient()

        val jsonObject = JSONObject()
        jsonObject.put("email",username.text.toString())
        jsonObject.put("password", password.text.toString())
        val responseBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .post(responseBody)
            .url("https://reqres.in/api/login")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
                runOnUiThread(Runnable {
                    dialog!!.dismiss()

                    Toast.makeText(this@LoginScreenActivity, "fail.", Toast.LENGTH_SHORT).show()

                })
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
                val body = response.body!!.string()
                var jsonObject :JSONObject
                runOnUiThread(Runnable {
                    dialog!!.dismiss()

                    Log.d("TAG", "onResponse: "+response.toString());
                    try {

                   // var body = response.peekBody(Long.MAX_VALUE).string()
                    jsonObject = JSONObject(body)
                    val status = jsonObject.optString("token")

                    val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    editor.putString("token",status)
                    editor.apply()
                    editor.commit()

                    nextScreen()

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }

                })

            }
        })
    }

    fun nextScreen()
    {
        val nextScreenIntent = Intent(this, HomeScreenActivity::class.java)
        nextScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        nextScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(nextScreenIntent)
        finish()
    }
}