package com.example.medilabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.medilabapp.Adapter.bookAdapter
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.helpers.PrefsHelper
import com.example.medilabapp.models.Bookings
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

class MyBookings : AppCompatActivity() {
    lateinit var itemList: List<Bookings>
    lateinit var bookAdapter: bookAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var swiperefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
        progress = findViewById(R.id.progress)
        recyclerView = findViewById(R.id.recycler)
        bookAdapter = bookAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        post_fetch()

        swiperefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swiperefresh.setOnRefreshListener {
            post_fetch()// fetch data again
        }//end refresh

        //Filter labs
        val etsearch = findViewById<EditText>(R.id.etsearch)
        etsearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(texttyped: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter(texttyped.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }//end oncreate


    //here
    fun post_fetch(){
        val api = "https://modcom.pythonanywhere.com/api/mybookings"
        //Above APi needs a Body, So we have to build it
        val body = JSONObject()
        //Retrieve the id from Prefs
        val member_id = PrefsHelper.getPrefs(applicationContext,"member_id")
        //provide the ID to the API
        body.put("member_id", member_id) //NB: 4 is static
        val helper = ApiHelper(applicationContext)
        helper.post(api, body, object : ApiHelper.CallBack{
            override fun onSuccess(result: JSONArray?) {
                val gson = GsonBuilder().create()
                itemList = gson.fromJson(result.toString(),
                    Array<Bookings>::class.java).toList()
                //Finally, our adapter has the data
                bookAdapter.setListItems(itemList)
                //For the sake of recycling/Looping items, add the adapter to recycler
                recyclerView.adapter = bookAdapter
                progress.visibility = View.GONE
                swiperefresh.isRefreshing = false
            }
            override fun onSuccess(result: JSONObject?) {
                Toast.makeText(applicationContext, result.toString(),
                    Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }

            override fun onFailure(result: String?) {
                Toast.makeText(applicationContext, "Error:"+result.toString(),
                    Toast.LENGTH_SHORT).show()
                val json = JSONObject(result.toString())
                val msg = json.opt("msg")
                //TODO
                if (msg == "Token has Expired"){
                    PrefsHelper.clearPrefs(applicationContext)
                    startActivity(Intent(applicationContext, SignIn::class.java))
                    finishAffinity()
                }
                Log.d("failureerrors", result.toString())
            }
        })
    }//end fetch

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Bookings> = ArrayList()
        // running a for loop to compare elements.
        for (item in itemList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.invoice_no.lowercase().contains(text.lowercase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
            bookAdapter.filterList(filteredlist)
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            bookAdapter.filterList(filteredlist)
        }
    }
}//end class