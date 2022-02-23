package com.example.shopapp.data.firestore

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.shopapp.common.Constants
import com.example.shopapp.domain.models.Users
import com.example.shopapp.presentation.ui.LoginFragment
import com.example.shopapp.presentation.ui.RegistrationFragment
import com.example.shopapp.presentation.ui.prefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {

    private val fireStore = FirebaseFirestore.getInstance()

    fun registerUser(registrationFragment: RegistrationFragment, userInfo: Users) {

        fireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                registrationFragment.userRegistrationSuccessful()
            }
            .addOnFailureListener {
                registrationFragment.hideProgressDialog()
                Log.e("registration", "Error while registering the user")
            }
    }

   private  fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUsersDetails(fragment: Fragment) {

        fireStore.collection(Constants.USERS)

            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.activity?.javaClass?.simpleName, document.toString())

                val user = document.toObject(Users::class.java)!!

                prefs.name = "${user.firstName} ${user.lastName}"

                when(fragment) {
                    is LoginFragment -> {
                     fragment.userLoggedInSuccessful(user)
                    }
                }
            }.addOnFailureListener {
                e->
            when(fragment){
                is LoginFragment -> {
                    fragment.hideProgressDialog()
                }
            }
                Log.e("registration2","Error while registering the user $e")
            }
    }
}