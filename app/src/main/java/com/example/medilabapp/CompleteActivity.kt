package com.example.medilabapp
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.medilabapp.Constants.Constants
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.helpers.PrefsHelper
import com.example.medilabapp.helpers.SQLiteCartHelper
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)
        // Get the required payload to make booking from the Shared preferences
        // Payload
        //Lets get to SQLIte, fetch the Tests Picked by user
        val sqllitehelper = SQLiteCartHelper(applicationContext)
        val items = sqllitehelper.getAllItems()
        val invoice_no = generateInvoiceNumber() //Autogenerate
        //Save Invoice No to Prefs
        PrefsHelper.savePrefs(applicationContext, "invoice_no", invoice_no)
        val numItems = items.size
        items.forEachIndexed { index, labTests ->
            //How many items do you have?
            // Capture test ID/Lab ID at a given Loop
            val test_id = labTests.test_id
            val lab_id = labTests.lab_id

            val member_id = PrefsHelper.getPrefs(this, "member_id")
            val date = PrefsHelper.getPrefs(this, "date")
            val time = PrefsHelper.getPrefs(this, "time")
            val booked_for = PrefsHelper.getPrefs(this, "booked_for")
            val where_taken = PrefsHelper.getPrefs(this, "where_taken")
            val latitude = PrefsHelper.getPrefs(this, "latitude")
            val longitude = PrefsHelper.getPrefs(this, "longitude")
            val dependant_id = PrefsHelper.getPrefs(this, "dependant_id")
//            val invoice_no = generateInvoiceNumber() //Autogenerate

            val helper = ApiHelper(this)
            val api = Constants.BASE_URL + "/make_booking"
            val body = JSONObject()
            body.put("member_id", member_id)
            body.put("appointment_date", date)
            body.put("appointment_time", time)
            body.put("booked_for", booked_for)
            body.put("where_taken", where_taken)
            body.put("latitude", latitude)
            body.put("longitude", longitude)
            body.put("dependant_id", dependant_id)
            body.put("test_id", test_id)
            body.put("lab_id", lab_id)
            body.put("invoice_no", invoice_no)

            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONObject?) {
                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext,"Error:"+result.toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("failureerrors", result.toString())

                }

                override fun onSuccess(result: JSONArray?) {

                }
            })//end post
            //Index counts from zero
            if (index == (numItems-1)){
                Toast.makeText(applicationContext, "Processing Done",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Payment::class.java))
                finish()
                //Make sure booking works
                //Create a single item XML for singlebooking.xml
                //include appointment_date, appointment_time, status in singlebooking.xml
                //Do a booking Model should follow the mybooking API response
                //DO AN ADAPTER
                //Do MyBookings Activity, to show the booking
                //Ref: ViewDependants
                //Intent from Screen2, Skip, using Skip button
                //Payment, Themes, Publishing
                //MyBooking
            }
        }//end for each
    }//end oncreate

    fun generateInvoiceNumber(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val currentTime = Date()
        val timestamp = dateFormat.format(currentTime)

        // You can use a random number or a sequential number to add uniqueness to the invoice number
        // For example, using a random number:
        val random = Random()
        val randomNumber = random.nextInt(1000) // Change the upper bound as needed

        // Combine the timestamp and random number to create the invoice number
        return "INV-$timestamp-$randomNumber"
    }
    //github.com/modcomlearning/MediLabApp
}//end class