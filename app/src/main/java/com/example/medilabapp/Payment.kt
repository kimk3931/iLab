package com.example.medilabapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.medilabapp.helpers.ApiHelper
import com.example.medilabapp.helpers.PrefsHelper
import com.example.medilabapp.helpers.SQLiteCartHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray
import org.json.JSONObject

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val textpay = findViewById<MaterialTextView>(R.id.textpay)
        val helper = SQLiteCartHelper(applicationContext)
        textpay.text = "Please Pay KES "+helper.totalCost()
        val btnpay = findViewById<MaterialButton>(R.id.btnpay)
        btnpay.setOnClickListener {
            val phone = findViewById<TextInputEditText>(R.id.phone)
            val api = "https://CollinsNgunyi1921.pythonanywhere.com/api/make_payment"
            val body = JSONObject()
            body.put("phone", phone.text.toString())//254
            //body.put("amount", helper.totalCost().toString())
            body.put("amount", "1")
            val invoice_no = PrefsHelper.getPrefs(this, "invoice_no")
            //provide it as a body to payment API
            body.put("invoice_no", invoice_no)//get from prefs
            // Where did the Money go? to Till Number
            //Did we update our databases? No
            val apihelper = ApiHelper(applicationContext)
            apihelper.post2(api, body, object : ApiHelper.CallBack{
                override fun onSuccess(result: JSONArray?) {

                }

                override fun onSuccess(result: JSONObject?) {
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }
                //Videos, Publishing on Play Store.
                //Github.com.
                //254729
            })
        }//end listener
    }//end oncreate
}//end class