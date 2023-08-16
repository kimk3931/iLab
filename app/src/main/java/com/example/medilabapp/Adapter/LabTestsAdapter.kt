package com.example.medilabapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.medilabapp.LabTestsActivity
import com.example.medilabapp.R
import com.example.medilabapp.SingleLabTest
import com.example.medilabapp.models.Lab
import com.example.medilabapp.models.LabTests
import com.google.android.material.textview.MaterialTextView

class LabTestsAdapter(var context: Context):
    RecyclerView.Adapter<LabTestsAdapter.ViewHolder>() {

    //Create a list and connect it with the model
    var itemlist : List<LabTests> = listOf() //Its empty

    //Create a Class, Will hld the views in single_lab xml
    inner class ViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabTestsAdapter.ViewHolder {
        //access/inflate te single lab xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_labtests,
            parent, false)

        return ViewHolder(view)  //pass the single lab to Viewholder
    }

    override fun onBindViewHolder(holder: LabTestsAdapter.ViewHolder, position: Int) {
        //Find the 3 text-views
        val test_name = holder.itemView.findViewById<MaterialTextView>(R.id.test_name)
        val test_description = holder.itemView.findViewById<MaterialTextView>(R.id.test_description)
        val test_cost = holder.itemView.findViewById<MaterialTextView>(R.id.test_cost)

        val item = itemlist[position]
        test_name.text = item.test_name
        test_description.text = item. test_description
        test_cost.text = item.test_cost+" KES"
        holder.itemView.setOnClickListener {
            val i = Intent(context, SingleLabTest::class.java)
            i.putExtra( "lab_id", item.lab_id)
            i.putExtra( "test_id", item.test_id)
            i.putExtra( "test_discount", item.test_discount)
            i.putExtra( "test_cost", item.test_cost)
            i.putExtra( "test_name", item.test_name)
            i.putExtra( "test_description", item.test_description)
            i.putExtra( "availability", item.availability)
            i.putExtra( "more_info", item.more_info)
            i.putExtra( "reg_date", item.reg_date)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }





//        Toast.makeText(context, "yyy"+item.test_cost, Toast.LENGTH_SHORT)
    }

    override fun getItemCount(): Int {
        return itemlist.size  //Count how many items in the list
    }

    //This is for filtering
    fun filterList(filterList: List<LabTests>){
        itemlist = filterList
        notifyDataSetChanged()
    }

    // item list is empty!!!
    // Get data from the API, then bring it to below function
    //The data you pass here must follow the lab model
    fun setListItems(data: List<LabTests>){
        itemlist = data //map or link the data to itemlist
        notifyDataSetChanged()
    //Tell this adapter class that now itemlist is loaded
    }

}