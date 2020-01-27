package com.example.tempstuff



//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.widget.Toast
//import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;


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

            // Showing the user input
            if(textFirstName==null)

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
