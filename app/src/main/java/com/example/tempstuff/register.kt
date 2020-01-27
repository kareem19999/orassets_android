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

    override fun onCreate(savedInstanceState: Bundle?)
    {
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




        createButton.setOnClickListener {

            // Getting the user input
            val textFirstName = inputFirstName.text
            val textMiddleName = inputMiddleName.text
            val textLastName = inputLastName.text
            val textDepartment = inputDepartment.text
            val textPassword = inputPassword.text
            val textConfirmPassword = inputConfirmPassword.text
            val textUsername = inputUsername.text

            // check if all required fields are filled and passwords match
            if(textFirstName!=null && textDepartment!=null && textLastName!=null && textPassword!=null && textConfirmPassword!=null && textUsername!=null && textPassword == textConfirmPassword)
            {   //Check if existing user
                val NewUser = hashMapOf(
                    "FName" to textFirstName,
                    "LName" to textMiddleName,
                    "MName" to textLastName,
                    "Department" to textDepartment,
                    "Password" to textPassword
                )
                val db = FirebaseFirestore.getInstance()
                db.collection("Users").document(textUsername.toString())
                    .set(NewUser)
                    .addOnSuccessListener { Log.d("Added a new user", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("Error adding a new user", "Error writing document", e) }

                //val intent = Intent(this@register, MainActivity::class.java)
                //startActivity(intent)
            }
            else if (textPassword != textConfirmPassword)
                Toast.makeText(this, "Unmatched passwords", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Fill in the required information", Toast.LENGTH_SHORT).show()


        }


        showHideBtn.setOnClickListener {
            if(showHideBtn.text.toString().equals("Show")){
                inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                inputConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showHideBtn.text = "Hide"
            } else{
                inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                inputConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showHideBtn.text = "Show"
            }
        }



    }
}
