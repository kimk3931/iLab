package com.example.medilabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.medilabapp.helpers.NetworkHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView

class Screen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen2)
        if (NetworkHelper.checkForInternet(applicationContext)){
            Toast.makeText(applicationContext, "You are Connected",
            Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(applicationContext, "You are Not Connected",
            Toast.LENGTH_SHORT).show()
            //kill the app, Intent to
            val builder = AlertDialog.Builder(this@Screen2)
            builder.setTitle("Internet")
            builder.setMessage("You are Not Connected\n Please Connect to Internet")
            builder.setPositiveButton("OK") {dialog,which ->
                finishAffinity()
            }//end of dialog
            builder.setCancelable(false)
            builder.show()
        }

        val skip2 = findViewById<MaterialTextView>(R.id.skip2)
        skip2.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        val fab2 = findViewById<FloatingActionButton>(R.id.fab2)
        fab2.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}