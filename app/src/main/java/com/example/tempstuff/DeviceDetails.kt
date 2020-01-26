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
                    val textView = findViewById<TextView>(R.id.DeviceNameView).apply {
                    text= theDevice?.Name}
                } }else {
                    Log.d("Not found", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error ", "get failed with ", exception)
            }
    }
}

