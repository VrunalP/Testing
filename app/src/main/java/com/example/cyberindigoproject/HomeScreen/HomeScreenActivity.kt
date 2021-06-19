package com.example.cyberindigoproject.HomeScreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyberindigoproject.CurrentLocation.CurrentLocationActivity
import com.example.cyberindigoproject.HomeScreen.Adapter.UserData
import com.example.cyberindigoproject.HomeScreen.Adapter.UsersAdapter
import com.example.cyberindigoproject.LoginScreen.LoginScreenActivity
import com.example.cyberindigoproject.R
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.activity_login_screen.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class HomeScreenActivity : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"
    var dialog : Dialog? = null

    var result = ArrayList<UserData>()

    var adapter : UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_screen)


        dialog = Dialog(this@HomeScreenActivity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_loading)


        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = layoutManager
        //recyclerView?.itemAnimator = DefaultItemAnimator()
        adapter = UsersAdapter(this@HomeScreenActivity,result)

        recyclerView?.adapter = adapter

        getUserList()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //adapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
              Log.d("onQueryTextChange", "query: " + newText)

                adapter?.filter?.filter(newText)
                return false
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> currentLocationScreen()

            R.id.action_search -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    fun currentLocationScreen()
    {
        val nextScreenIntent = Intent(this, CurrentLocationActivity::class.java)
        startActivity(nextScreenIntent)
    }

    fun logout()
    {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val nextScreenIntent = Intent(this, LoginScreenActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }

    fun getUserList()
    {

        dialog?.show()
        val okHttpClient = OkHttpClient()

        val request = Request.Builder()
            .get()
            .url("https://reqres.in/api/users?page=2")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
                runOnUiThread(Runnable {

                    dialog?.dismiss()

                    Toast.makeText(this@HomeScreenActivity, "fail.", Toast.LENGTH_SHORT).show()

                })
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
                val body = response.body!!.string()
                var jsonObject : JSONObject


                runOnUiThread(Runnable {

                    Log.d("TAG", "onResponse: "+response.toString());
                    try {
                        dialog?.dismiss()

                        // var body = response.peekBody(Long.MAX_VALUE).string()
                        jsonObject = JSONObject(body)

                        var jsonArray = jsonObject.getJSONArray("data")


                        for (i in 0 until jsonArray.length()) {

                            var jsonObject2 =jsonArray.getJSONObject(i)

                            var user: UserData = UserData(jsonObject2.getInt("id"),jsonObject2.getString("email")
                                ,jsonObject2.getString("first_name")+" "+jsonObject2.optString("last_name")
                                , jsonObject2.getString("avatar"))
                            result.add(user)
                        }

                        adapter?.notifyDataSetChanged()

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }

                })

            }
        })
    }
}