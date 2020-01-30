package com.example.tempstuff



//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
//import android.widget.Toast
//import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

//        button.setOnClickListener(){
        // finding the edit text
        val inputFirstName = findViewById<EditText>(R.id.editFirstName)
        val inputMiddleName = findViewById<EditText>(R.id.editMiddleName)
        val inputLastName = findViewById<EditText>(R.id.editLastName)
        val inputDepartment = findViewById<EditText>(R.id.editDepartment)
        val inputPassword = findViewById<EditText>(R.id.editPassword)
        val inputConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val inputUsername = findViewById<EditText>(R.id.editUsername)

        val createButton = findViewById<Button>(R.id.Create)
        val showHideBtn = findViewById<Button>(R.id.showHideBtn)
        val Login = findViewById<Button>(R.id.Login)




        createButton.setOnClickListener {
            // Getting the user input
            var textFirstName = inputFirstName.text
            var textMiddleName = inputMiddleName.text
            var textLastName = inputLastName.text
            var textDepartment = inputDepartment.text
            var textPassword = inputPassword.text
            var textConfirmPassword = inputConfirmPassword.text
            var textUsername = inputUsername.text
            //Prepare check for uniqueness of username
            var db = FirebaseFirestore.getInstance()
            //Must exist in order for app to not crash
            var textstring = textUsername.toString()

            if (textstring.isNotEmpty()) {
                var theUser: MyListUser?
                var docRef = db.collection("Users").document(textUsername.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document == null) {

                        } else
                        {
                            Log.d(
                                "Done",
                                "DocumentSnapshot data: ${document.data}")
                            //Check username
                            docRef.get().addOnSuccessListener { documentSnapshot ->
                                theUser = documentSnapshot.toObject(MyListUser::class.java)
                                //Test if empty

                                var FNameString = theUser?.FName.toString()
                                if (FNameString=="null" || FNameString=="" ) {
                                    // check if all required fields are filled and passwords match
                                    if (textFirstName.toString().isNotEmpty()&& textDepartment.toString().isNotEmpty() && textLastName.toString().isNotEmpty() && textPassword.toString().isNotEmpty() && textConfirmPassword.toString().isNotEmpty()  && textUsername.toString().isNotEmpty() && (textPassword.toString() == textConfirmPassword.toString()))
                                    {

                                        //That means there is no object, so user is free  and all details are added
                                        val NewUser = hashMapOf(
                                            "FName" to textFirstName.toString(),
                                            "LName" to textMiddleName.toString(),
                                            "MName" to textLastName.toString(),
                                            "Department" to textDepartment.toString(),
                                            "Password" to textPassword.toString()
                                        )
                                        val db = FirebaseFirestore.getInstance()
                                        db.collection("Users").document(textUsername.toString())
                                            .set(NewUser)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Added a new user",
                                                    "DocumentSnapshot successfully written!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "Error adding a new user",
                                                    "Error writing document",
                                                    e
                                                )
                                            }
                                        //Should go to login screen
                                        Toast.makeText(this, "New User", Toast.LENGTH_SHORT)
                                            .show()
                                    } else if (textPassword != textConfirmPassword)
                                    {
                                        Toast.makeText(
                                            this,
                                            "Unmatched passwords",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Fill in the required information",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else
                                {
                                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
            }

            showHideBtn.setOnClickListener {
                if (showHideBtn.text.toString().equals("Show")) {
                    inputPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    inputConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    showHideBtn.text = "Hide"
                } else {
                    inputPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    inputConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    showHideBtn.text = "Show"
                }
            }
        }
        Login.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
}
