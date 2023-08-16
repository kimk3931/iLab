package com.example.medilabapp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.medilabapp.Adapter.LabAdapter
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.helpers.PrefsHelper
import com.example.medilabapp.helpers.SQLiteCartHelper
import com.example.medilabapp.models.Lab
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    //Global Declaration - they can be accessed all over this class
    lateinit var itemList: List<Lab>
    lateinit var labAdapter: LabAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var swiperefresh: SwipeRefreshLayout



    fun update(){
        val user = findViewById<MaterialTextView>(R.id.user)
        val signin = findViewById<MaterialButton>(R.id.signin)
        val signout = findViewById<MaterialButton>(R.id.signout)
        val profile = findViewById<MaterialButton>(R.id.profile)

        signin.visibility = View.GONE
        signout.visibility = View.GONE
        profile.visibility = View.GONE

        val token = PrefsHelper.getPrefs(applicationContext, "refresh_token")
        if (token.isEmpty()){ //Token Not Available
            user.text = "Not Logged In"
            signin.visibility = View.VISIBLE
            signin.setOnClickListener {
                startActivity(Intent(applicationContext, SignIn::class.java))
            }
        }
        else{ //token available
            profile.visibility = View.VISIBLE
            profile.setOnClickListener {
                startActivity(Intent(applicationContext, MemberProfile::class.java))
            }//end

            val surname = PrefsHelper.getPrefs(applicationContext, "surname")
            user.text = "Welcome $surname"
            signout.visibility = View.VISIBLE
            signout.setOnClickListener {
                PrefsHelper.clearPrefs(applicationContext)
                startActivity(intent)
                finishAffinity()
            }
        }
    }//end
    fun requestLocation(){
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                123)

        }//end if
        else{

        }

    }//end function

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocation()
        update()

        progress = findViewById(R.id.progress)
        recyclerView = findViewById(R.id.recycler)
        labAdapter = LabAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        //Call the function
        fetchData()

        swiperefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swiperefresh.setOnRefreshListener {
            fetchData() //fetch data again
        } //end refresh

        //Filter Labs
        val etsearch = findViewById<EditText>(R.id.etsearch)
        etsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(texttyped: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter(texttyped.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



    } // end Oncreate

    fun fetchData() {
        //Go to the API get the data
        val api = "https://modcom.pythonanywhere.com/api/laboratories"
        val helper = ApiHelper(applicationContext)
        helper.get(api, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {
                //Take Above result to the adapter
                //Convert Above Result from JSON array to LIST<lab>
                val gson = GsonBuilder().create()
                itemList = gson.fromJson(
                    result.toString(),
                    Array<Lab>::class.java
                ).toList()
                //Finally , our adapter has the data
                labAdapter.setListItems(itemList)
                //For the sake of recycling/ Looping items, add the adapter to the recycler
                recyclerView.adapter = labAdapter
                progress.visibility = View.GONE
                swiperefresh.isRefreshing = false
            }//end

            override fun onSuccess(result: JSONObject?) {
                TODO("Not yet implemented")
            }

            override fun onFailure(result: String?) {
                Toast.makeText(applicationContext,"Error:"+result.toString(),
                    Toast.LENGTH_SHORT).show()
                Log.d("failureerrors", result.toString())

                progress.visibility = View.GONE

            }

        })


    } //end of fetchData

    //Filter Function
    //Filter
    private fun filter(text: String) {
// creating a new array list to filter our data.
        val filteredlist: ArrayList<Lab> = ArrayList()
// running a for loop to compare elements.
        for (item in itemList) {
// checking if the entered string matched with any item of our recycler view.
            if (item.lab_name.lowercase().contains(text.lowercase())) {
// if the item is matched we are
// adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
// if no item is added in filtered list we are
// displaying a toast message as no data found.
//Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
            labAdapter.filterList(filteredlist)
        } else {
// at last we are passing that filtered
// list to our adapter class.
            labAdapter.filterList(filteredlist)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val item: MenuItem = menu!!.findItem(R.id.mycart)
        item.setActionView(R.layout.design)
        val actionview: View? = item.actionView
        val number = actionview?.findViewById<TextView>(R.id.badge)
        val image = actionview?.findViewById<ImageView>(R.id.image)
        image?.setOnClickListener{
            startActivity(Intent(applicationContext, MyCart::class.java))

        }
        val helper = SQLiteCartHelper(applicationContext)
        number?.text = ""+helper.getNumItems()
        return super.onCreateOptionsMenu(menu)
    }





}