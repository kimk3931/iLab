package com.example.medilabapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medilabapp.R
import com.example.medilabapp.models.Bookings
import com.google.android.material.textview.MaterialTextView

class bookAdapter(var context: Context):
    RecyclerView.Adapter<bookAdapter.ViewHolder>() {

    //Create a List and connect it with our model
    var itemList : List<Bookings> = listOf() //Its empty

    //Create a Class here, will hold our views in single_lab xml
    inner class  ViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookAdapter.ViewHolder {
        //access/inflate the single lab xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_bookings,
            parent, false)

        return ViewHolder(view) //pass the single lab to ViewHolder
    }

    override fun onBindViewHolder(holder: bookAdapter.ViewHolder, position: Int) {
        //Find your 3 text views
        val appointment_date = holder.itemView.findViewById<MaterialTextView>(R.id.appointment_date)
        val appointment_time = holder.itemView.findViewById<MaterialTextView>(R.id.appointment_time)
        val status = holder.itemView.findViewById<MaterialTextView>(R.id.status)
        val invoice_no = holder.itemView.findViewById<MaterialTextView>(R.id.invoice_no)
        //Assume one Lab
        val item = itemList[position]
        appointment_date.text = item.appointment_date
        appointment_time.text = item.appointment_time
        status.text = item.status
        invoice_no.text = item.invoice_no
//         holder.itemView.setOnClickListener {
//             ////Is confirmation dialog Needed? Research
//                 PrefsHelper.savePrefs(context, "dependant_id", item.dependant_id)
//                 val i = Intent(context, CheckoutStep2GPS::class.java)
//                 i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                 context.startActivity(i)
//         }//end Listner
    }//end bind

    override fun getItemCount(): Int {
        return itemList.size  //Count how may Items in the List
    }

    //This is for filtering data
    fun filterList(filterList: List<Bookings>){
        itemList = filterList
        notifyDataSetChanged()
    }


    //Earlier we mentioned item List is empty!
    //We will get data from our APi, then bring it to below function
    //The data you bring here must follow the Lab model
    fun setListItems(data: List<Bookings>){
        itemList = data //map/link the data to itemlist
        notifyDataSetChanged()
        //Tell this adapter class that now itemList is loaded with data
    }
    //justpaste.it/cgaym
}