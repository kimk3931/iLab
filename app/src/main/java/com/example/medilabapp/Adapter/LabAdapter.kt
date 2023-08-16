package com.example.medilabapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medilabapp.LabTestsActivity
import com.example.medilabapp.R
import com.example.medilabapp.models.Lab
import com.google.android.material.textview.MaterialTextView

class LabAdapter(var context: Context):
    RecyclerView.Adapter<LabAdapter.ViewHolder>() {

    //Create a list and connect it with the model
    var itemlist : List<Lab> = listOf() //Its empty

    //Create a Class, Will hld the views in single_lab xml
    inner class ViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabAdapter.ViewHolder {
        //access/inflate te single lab xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_lab,
            parent, false)

        return ViewHolder(view)  //pass the single lab to Viewholder
    }

    override fun onBindViewHolder(holder: LabAdapter.ViewHolder, position: Int) {
        //Find the 3 text-views
        val lab_name = holder.itemView.findViewById<MaterialTextView>(R.id.lab_name)
        val permit_id = holder.itemView.findViewById<MaterialTextView>(R.id.permit_id)
        val email = holder.itemView.findViewById<MaterialTextView>(R.id.email)

        val lab = itemlist[position]
        lab_name.text = lab.lab_name
        permit_id.text = lab.permit_id
        email.text = lab.email

        //When
        holder.itemView.setOnClickListener{
            //carry lab_id
            //carry with bundles
            val id = lab.lab_id
            val name = lab.lab_name
            //pass the ID
            //save id to prefs

            val i = Intent(context, LabTestsActivity::class.java)
            i.putExtra( "key1", id)
            i.putExtra( "key2", name)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)

        }
    }

    override fun getItemCount(): Int {
        return itemlist.size  //Count how many items in the list
    }

    //This is for filtering
    fun filterList(filterList: List<Lab>){
        itemlist = filterList
        notifyDataSetChanged()
    }

    // item list is empty!!!
    // Get data from the API, then bring it to below function
    //The data you pass here must follow the lab model
    fun setListItems(data: List<Lab>){
        itemlist = data //map or link the data to itemlist
        notifyDataSetChanged()
    //Tell this adapter class that now itemlist is loaded
    }

}