package com.example.tempstuff

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tempstuff.User
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

//import kotlinx.android.synthetic.main.layout_log_in.*

class LoginActivity : AppCompatActivity() {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        logIn.setOnClickListener {
            if (isContentFilledCorrectly()) {
                signInUser(email.text.toString(), password.text.toString())
            }
        }
    }
    var logIn=findViewById(R.id.button) as Button
    var email=findViewById(R.id.editText) as EditText
    var password=findViewById(R.id.editText2) as EditText
    private fun isContentFilledCorrectly(): Boolean {
        var result = true

        if (email.length() == 0) {
            Toast.makeText(this, "Email not entered", Toast.LENGTH_LONG).show()

            result = false
        }

        if (password.length() == 0) {
            Toast.makeText(this, "No password entered", Toast.LENGTH_LONG).show()
            result = false
        }

        return result
    }

    private fun signInUser(email: String, password: String) {
        logIn.visibility = View.INVISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User sign in succeeded.
                    var user: User?
                    getUser(auth.uid!!, OnSuccessListener { result ->
                        user = User(
                            result["name"] as String
                        )
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("Username",user?.name)
                        startActivity(intent)
                    })


                    finishAffinity()
                } else {
                    // User sign in failed.
                    logIn.visibility = View.VISIBLE

                    when {
                        task.exception is FirebaseAuthInvalidUserException ->
                            Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show()
                        task.exception is FirebaseAuthInvalidCredentialsException ->
                            Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show()
                        else ->
                            Toast.makeText(
                                baseContext,
                                "Log in failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
    }
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun getUser(userId: String, successListener: OnSuccessListener<DocumentSnapshot>) {
        db.collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener(successListener)
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
    fun saveProfile(name: String, phoneNumber: String, profilePhoto: String? = null, busLine: String? = null,context: Context) {
        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(LocalDataManager.USER_NAME, name)
        editor.apply()
    }
}
data class User(
    val name: String

)