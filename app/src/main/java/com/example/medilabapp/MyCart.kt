package com.example.medilabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medilabapp.Adapter.LabTestsAdapter
import com.example.medilabapp.Adapter.LabTestsCartAdapter
import com.example.medilabapp.helpers.PrefsHelper
import com.example.medilabapp.helpers.SQLiteCartHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class MyCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        //put total cost in a textview

        val checkout = findViewById<MaterialButton>(R.id.checkout)
        val helper = SQLiteCartHelper(applicationContext)
        if (helper.totalCost() == 0.0){
            checkout.visibility = View.GONE
        }//end
        checkout.setOnClickListener {
            val token =PrefsHelper.getPrefs(applicationContext,"refresh_token")
            if (token.isEmpty()){
                Toast.makeText(applicationContext, "Not Logged In",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, SignIn::class.java))
                finish()
            }
            else{
                //TODO
                startActivity(Intent(applicationContext, CheckoutStep1::class.java))
                Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
            }

        }//end

        val total = findViewById<MaterialTextView>(R.id.total)

        total.text = "Total: "+helper.totalCost()

        //Find The Recycler

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)
        //Access adapter and provide it with from getAllItems
        if(helper.getNumItems() == 0){
            Toast.makeText(applicationContext, "Your Cart is Empty", Toast.LENGTH_SHORT).show()
        }
        else{
        val adapter = LabTestsCartAdapter(applicationContext)
        adapter.setListItems(helper.getAllItems())//pass data
        recycler.adapter = adapter //link adapter to recycler
        }
    }

    //Activate Options Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clearcart){
            val helper = SQLiteCartHelper(applicationContext)
            helper.clearCart()
        }
        if (item.itemId == R.id.backtolabs){
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
        val i = Intent(applicationContext, MainActivity::class.java)
//        i.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finishAffinity()
    }
}