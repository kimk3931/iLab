package com.example.medilabapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.medilabapp.Adapter.LabAdapter
import com.example.medilabapp.Adapter.LabTestsAdapter
import com.example.medilabapp.Constants.Constants
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.models.Lab
import com.example.medilabapp.models.LabTests
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

class LabTestsActivity : AppCompatActivity() {
    lateinit var itemList: List<LabTests>
    lateinit var labtestAdapter: LabTestsAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var swiperefresh: SwipeRefreshLayout





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_tests)
        progress = findViewById(R.id.progress)
        recyclerView = findViewById(R.id.recycler)
        labtestAdapter = LabTestsAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        post_fetch()
        swiperefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swiperefresh.setOnRefreshListener {
            post_fetch()

        } //end refresh
    } //end OnCreate

    fun post_fetch(){
        val api = Constants.BASE_URL+"/lab_tests"
        // Above API needs a body
        val body = JSONObject()
        //Retrieve the id from Intent Bundle Extras

        val id = intent.extras?.getString("key1")
        //Toast.makeText(applicationContext, "ID $id", Toast.LENGTH_SHORT).show()
        // provide the ID to the API
        body.put("lab_id", id) //NB: 4 is static
        val helper = ApiHelper(applicationContext)
        helper.post(api, body, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {
                val gson = GsonBuilder().create()
                itemList = gson.fromJson(result.toString(),
                 Array<LabTests>::class.java).toList()
                //Adapter has Data
                labtestAdapter.setListItems(itemList)
                //sake of recycling and looping
                recyclerView.adapter = labtestAdapter
                progress.visibility = View.GONE
                swiperefresh.isRefreshing = false

            }

            override fun onSuccess(result: JSONObject?) {
                Toast.makeText(applicationContext, result.toString(),
                Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(result: String?) {
                Toast.makeText(applicationContext,"Error:"+result.toString(),
                    Toast.LENGTH_SHORT).show()
                Log.d("failureerrors", result.toString())

            }

        })


    }//End Fetch



}
