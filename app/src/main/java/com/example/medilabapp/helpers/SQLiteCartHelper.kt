package com.example.medilabapp.helpers

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.medilabapp.MainActivity
import com.example.medilabapp.MyCart
import com.example.medilabapp.models.LabTests

class SQLiteCartHelper(context: Context):
     SQLiteOpenHelper(context, "cart.db", null, 1){
    //Make context visible to other functions
     val context = context

    //SQLite helps store data Locally on your phone - You can CRUD
    override fun onCreate(sql: SQLiteDatabase?) {
        sql?.execSQL("CREATE TABLE IF NOT EXISTS cart(test_id Integer primary key, test_name varchar, test_cost Integer, lab_id Integer, test_description text)")
    }

    override fun onUpgrade(sql: SQLiteDatabase?, p1: Int, p2: Int) {
        sql?.execSQL("DROP TABLE IF EXISTS cart")
    }

    //INSERT Save to cart
    fun insert(test_id: String, test_name: String, test_cost: String,
               test_description: String, lab_id: String){
        val db = this.writableDatabase
        //Select Before insert see if ID already exists
        val values = ContentValues()
        values.put("test_id", test_id)
        values.put("test_name", test_name)
        values.put("test_cost", test_cost)
        values.put("test_description", test_description)
        values.put("lab_id", lab_id)
        //save
        val result:Long = db.insert("cart", null, values)
        if (result < 1 ){
//            println("Failed To Add")
            Toast.makeText(context, "Item Already In Cart", Toast.LENGTH_SHORT).show()
        }
        else{
//            println(" Item Added To Cart")
//            val i = Intent(context, MainActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(i)
            Toast.makeText(context, "Item Added To Cart", Toast.LENGTH_SHORT).show()
        }
    }//end

    //TODO GetRecords, Delete all, delete one, Gte Totals
   //Count how many items are there in the cart table
    fun getNumItems(): Int{
        val db = this.readableDatabase
        val result: Cursor = db.rawQuery("select * from cart", null)
        //return result count
        return result.count
    }//end

    // Clear All Records
    fun clearCart(){
        val db = this.writableDatabase
        db.delete("cart", null, null)
//        println("Cart Cleared")
        Toast.makeText(context, "Cart Cleared", Toast.LENGTH_SHORT).show()
        val i = Intent(context, MyCart::class.java)
        i.flags - Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)


    }//end

    fun clearCartById(test_id: String){
        val db = this.writableDatabase
        db.delete("cart", "test_id=?", arrayOf(test_id))
//        println("Item Id $test_id Removed")
        Toast.makeText(context, "Item Id $test_id Removed", Toast.LENGTH_SHORT).show()
        val i = Intent(context, MyCart::class.java)
        i.flags - Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)


    }//end

    fun totalCost(): Double{
        val db = this.readableDatabase
        val result: Cursor = db.rawQuery("select SUM(test_cost) from cart",
        null)
        var total: Double = 0.0
        while (result.moveToNext()){
            //The cursor returns the list of the test_cost
            //Retrieve the value from the first column of the current row

            total += result.getDouble(0)
        }//end while
        return total
    }//End

    fun getAllItems() : ArrayList<LabTests>{
        val db = this.writableDatabase

        val items = ArrayList<LabTests>()
        val result: Cursor = db.rawQuery("select * from cart", null)
        //Lets Add all rows into the items array list
        while (result.moveToNext())
        {
            val model = LabTests()
            //Map rows to the model
            model.test_id = result.getString(0) // Assume one row, test_id
            model.test_name = result.getString(1) // Assume one row, test_name
            model.test_cost = result.getString(2) // Assume one row, test_cost
            model.lab_id = result.getString(3) // Assume one row, lab_id
            model.test_description = result.getString(4) // Assume one row, test_description
            items.add(model) //add model to ArrayList

        }//end while
        return items
    }//end function

}