package com.example.tempstuff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide.init
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DeviceDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var id=intent.getStringExtra("ID")
        val borrow=intent.getStringExtra("Allow")
        val Username=intent.getStringExtra("username")
        val UType=intent.getStringExtra("Type")
        val db = FirebaseFirestore.getInstance()
        val DevicesRef = db.collection("Devices")
        //var   DeviceSearched= DevicesRef.whereEqualTo("Devices",id)
        val docRef = db.collection("Devices").document(id)
        var theDevice: MyList?
        val btn = findViewById(R.id.Borrow) as Button
        val EditDevice = findViewById(R.id.Edit) as Button
        btn.setVisibility(View.INVISIBLE)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Found", "DocumentSnapshot data: ${document.data}")
                    docRef.get() //Display Data
                        .addOnSuccessListener { documentSnapshot ->
                    theDevice= documentSnapshot.toObject(MyList::class.java)
                            if(theDevice?.AdminID=="null") {
                                Toast.makeText(this, "Device Not Found!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@DeviceDetails, MainActivity::class.java)
                                startActivity(intent)
                            }else {
                    Toast.makeText(this, "Device found", Toast.LENGTH_SHORT).show()
                    if(UType=="Admin")
                    {
                        EditDevice.visibility=View.VISIBLE
                    }else
                    {
                        EditDevice.visibility=View.INVISIBLE
                    }
                    //Decide whether button should appear or not
                    if(borrow=="Yes"){
                        Toast.makeText(this, borrow, Toast.LENGTH_SHORT).show()

                         btn.visibility=View.VISIBLE
                        //This will add to the list of waiting for approval, then return to activity main with a toast
                         btn.setOnClickListener {if(theDevice?.Availability==1) {
                             val NewLog = hashMapOf(
                                 "DeviceID" to id, //ID of device to be borrowed
                                 "Username" to Username, //Username of borrower
                                 "Approval" to 0, //Check approval (0,1)
                                 "Borrow" to 0 //Status, borrowed or returned (0 for approval, 1 for borrowed, 1 for returned)
                             )
                             val db = FirebaseFirestore.getInstance()
                             db.collection("Log").document()
                                 .set(NewLog)
                                 .addOnSuccessListener {
                                     Log.d(
                                         "Added a new log",
                                         "DocumentSnapshot successfully written!"
                                     )
                                 }
                                 .addOnFailureListener { e ->
                                     Log.w(
                                         "Error adding log",
                                         "Error writing document",
                                         e
                                     )
                                 }
                             //Change availability
                             db.collection("Devices").document(id)
                                 .update("Availability", 0)
                                 .addOnSuccessListener {
                                     Log.d(
                                         "Changed Availability",
                                         "DocumentSnapshot successfully written!"
                                     )
                                 }
                                 .addOnFailureListener { e ->
                                     Log.w(
                                         "Error Changing",
                                         "Error writing document",
                                         e
                                     )
                                 }
                             Toast.makeText(this, "Pending Approval", Toast.LENGTH_LONG).show()
                         }else{Toast.makeText(this, "Device is already in use!", Toast.LENGTH_LONG).show()}
                             val intent = Intent(this@DeviceDetails, MainActivity::class.java)
                             startActivity(intent)
                         }
                          }else{ btn.visibility=View.INVISIBLE}
                    val textView = findViewById<TextView>(R.id.PassedName).apply {
                        text= theDevice?.Name}
                    val textView2 = findViewById<TextView>(R.id.PassedType).apply {
                        text= theDevice?.Type}
                    val textView3 = findViewById<TextView>(R.id.PassedModel).apply {
                        text= theDevice?.Model}
                    val textView4 = findViewById<TextView>(R.id.PassedWindowsModel).apply {
                        text= theDevice?.Windows}
                    val textView5 = findViewById<TextView>(R.id.PassedDepartment).apply {
                        text= theDevice?.Department}
                    val textView6 = findViewById<TextView>(R.id.PassedAdminName).apply {
                        text= theDevice?.AdminID.toString()}
                    var check: String = ""
                    if(theDevice?.Availability==1) {check="Yes"}else{check="No"}
                    val textView7 = findViewById<TextView>(R.id.PassedAvailability).apply {
                        text= check}
                    val textView8 = findViewById<TextView>(R.id.PassedCondition).apply {
                        text= theDevice?.Condition}


                } }}else {
                    Log.d("Not found", "No such document")

                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error ", "get failed with ", exception)
            }
    }
}

