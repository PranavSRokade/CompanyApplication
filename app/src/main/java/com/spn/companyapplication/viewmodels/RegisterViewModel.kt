package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.widget.Toast
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.models.User
import com.spn.companyapplication.screens.Home

class RegisterViewModel(): ViewModel(){
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseFirestore = FirebaseFirestore.getInstance()

    var showRoleOptions by mutableStateOf(false)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var role by mutableStateOf("Select Role")
    var email by mutableStateOf("")

    val roleOptions = listOf("Admin", "Business Development Manager")

    var validate by mutableStateOf(false)

    var showLoader by mutableStateOf(false)

    fun usernameChange(text: String){
        username = text
    }

    fun passwordChange(text: String){
        password = text
    }

    fun roleChange(text: String){
        role = text
    }

    fun emailChange(text: String){
        email = text
    }

    fun nameChange(text: String){
        name = text
    }

    fun registerUser(activity: Activity){
        showLoader = true
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val userId = user?.uid ?: ""

                    val userInfo = User(name, role, email, username)

                    firebaseFirestore.collection("users")
                        .document(userId)
                        .set(userInfo)
                        .addOnSuccessListener {
                            val toast = "Registration Successful. You have been logged in."
                            val centeredText = SpannableString(toast)
                            centeredText.setSpan(
                                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toast.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )
                            Toast.makeText(activity, centeredText, Toast.LENGTH_SHORT).show()

                            activity.startActivity(Intent(activity, Home::class.java))
                            activity.finish()
                        }
                        .addOnFailureListener { e ->
                            showLoader = false

                            Log.d("TAG64", e.toString())
                            Toast.makeText(activity, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(activity, "Couldn't save user details.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{e ->
                showLoader = false

                Log.d("TAG85", e.toString())
                Toast.makeText(activity, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show()
            }
    }

    fun validation() {
        validate =
            !(username == "" || password == "" || role == "Select Role" || email == "" || name == "")
    }
}