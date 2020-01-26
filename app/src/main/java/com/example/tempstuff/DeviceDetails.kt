package com.example.tempstuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide.init
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DeviceDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)
        var id=intent.getStringExtra("ID")
        val db = FirebaseFirestore.getInstance()
        val DevicesRef = db.collection("Devices")
        //var   DeviceSearched= DevicesRef.whereEqualTo("Devices",id)
        val docRef = db.collection("Devices").document(id)
        var theDevice: MyList?
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Found", "DocumentSnapshot data: ${document.data}")
                    docRef.get() //Display Data
                        .addOnSuccessListener { documentSnapshot ->
                    theDevice= documentSnapshot.toObject(MyList::class.java)
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
                } }else {
                    Log.d("Not found", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error ", "get failed with ", exception)
            }
    }
}

