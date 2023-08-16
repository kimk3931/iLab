package com.example.medilabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.medilabapp.Constants.Constants
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.helpers.PrefsHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray
import org.json.JSONObject

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

         //link to register while in login.
        val linktosignup = findViewById<MaterialTextView>(R.id.linktosignup)
        linktosignup.setOnClickListener {
            startActivity(Intent(applicationContext, SignUp::class.java))
            finishAffinity()
        }
        val surname = findViewById<TextInputEditText>(R.id.surname)
        val password = findViewById<TextInputEditText>(R.id.password)
        val login = findViewById<MaterialButton>(R.id.login)

        login.setOnClickListener {  //Where do we close it
            val api = Constants.BASE_URL+"/member_signin"
            val helper = ApiHelper(applicationContext)
            val body = JSONObject()
            body.put("surname", surname.text.toString())
            body.put("password", password.text.toString())
            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONArray?) {

                }
                //user bob101
                //Qwerty1234
                override fun onSuccess(result: JSONObject?) {
                    Toast.makeText(applicationContext, result.toString(),
                    Toast.LENGTH_SHORT).show()
                    //consume the json access keys
                    if (result!!.has("refresh_token")){
                        val refresh_token = result.getString("refresh_token")
                        val access_token = result.getString("access_token")
                        val message = result.getString("message")

                        Toast.makeText(applicationContext, "Success",
                        Toast.LENGTH_SHORT).show()
                        PrefsHelper.savePrefs(applicationContext,
                        "refresh_token", refresh_token)
                        PrefsHelper.savePrefs(applicationContext,
                        "access_token", access_token)

                        val member = JSONObject(message)
                        val member_id = member.getString("member_id")
                        val email = member.getString("email")
                        val surname = member.getString("surname")


                        PrefsHelper.savePrefs(applicationContext,
                        "userObject", message)

                        PrefsHelper.savePrefs(applicationContext,
                            "member_id", member_id)

                        PrefsHelper.savePrefs(applicationContext,
                            "email", email)

                        PrefsHelper.savePrefs(applicationContext,
                            "surname", surname)

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finishAffinity()


                        //Proceed to homepage. We need to create it
                    }
                    else{
                        Toast.makeText(applicationContext,result.toString(),
                        Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext,"Error:"+result.toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("failureerrors", result.toString())

                }
            })


        }

    }
}