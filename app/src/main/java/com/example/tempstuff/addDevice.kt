package com.example.tempstuff

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_device.*

class addDevice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)
        setSupportActionBar(toolbar)
        val inputName = findViewById<EditText>(R.id.PassedName)
        val inputType = findViewById<EditText>(R.id.PassedType)
        val inputModel = findViewById<EditText>(R.id.PassedModel)
        val inputAvailability = findViewById<EditText>(R.id.PassedAvailability)
        val inputWindows = findViewById<EditText>(R.id.PassedWindowsModel)
        val inputDepartment = findViewById<EditText>(R.id.PassedDepartment)
        val inputAdminID = findViewById<EditText>(R.id.PassedAdminName)
        val createCondition = findViewById<Button>(R.id.Created)

        createCondition.setOnClickListener {
            // Getting the user input
            var textName = inputName.text
            var textType = inputType.text
            var textModel = inputModel.text
            var textAvailability = inputAvailability.text
            var textWindows = inputWindows.text
            var textDepartment = inputDepartment.text
            var textAdminID = inputAdminID.text
            //Prepare check for uniqueness of username
            var db = FirebaseFirestore.getInstance()
            //Must exist in order for app to not crash
            var textstring = textName.toString()

            if (textstring.isNotEmpty()) {
                var theUser: MyList?
                var docRef = db.collection("Devices").document(textName.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document == null) {

                        } else {
                            Log.d(
                                "Done",
                                "DocumentSnapshot data: ${document.data}"
                            )
                            //Check username
                            docRef.get().addOnSuccessListener { documentSnapshot ->
                                theUser = documentSnapshot.toObject(MyList::class.java)
                                //Test if empty
                                var aval = textAvailability.toString()
                                var NameString = theUser?.Name.toString()
                                if (NameString == "null" || NameString == "") {
                                    // check if all required fields are filled and passwords match
                                    if (textName.toString().isNotEmpty() && textDepartment.toString().isNotEmpty() && textType.toString().isNotEmpty() && textModel.toString().isNotEmpty() && textWindows.toString().isNotEmpty() && textAdminID.toString().isNotEmpty()) {

                                        //That means there is no object, so user is free  and all details are added
                                        val NewDevice = hashMapOf(
                                            "Name" to textName.toString(),
                                            "Type" to textType.toString(),
                                            "Model" to textModel.toString(),
                                            "Availability" to aval.toInt(),
                                            "Windows" to textWindows.toString(),
                                            "Department" to textDepartment.toString(),
                                            "AdminID" to textAdminID.toString()
                                        )
                                        val db = FirebaseFirestore.getInstance()
                                        db.collection("Devices").document()
                                            .set(NewDevice)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Added a new Device",
                                                    "DocumentSnapshot successfully written!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "Error adding",
                                                    "Error writing document",
                                                    e
                                                )
                                            }

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Device already exists",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }
                    }


            }
        }





    }
}
