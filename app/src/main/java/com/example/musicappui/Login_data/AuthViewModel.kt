package com.example.musicappui.Login_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    private val _userDetails = MutableLiveData<Result<Map<String, Any>>>()
    val userDetails: LiveData<Result<Map<String, Any>>> get() = _userDetails

    fun isUserLoggedIn(): Boolean {
        return userRepository.getCurrentUser() != null
    }


   fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            _authResult.value = result

            if (result is Result.Success) {
                userRepository.getCurrentUser()?.let {
                    _userDetails.value = userRepository.getUserDetails(it.uid)
                }
            }
        }
    }



    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            val result = userRepository.signUp(email, password, firstName, lastName)
            _authResult.value = result

            if (result is Result.Success) {
                userRepository.getCurrentUser()?.let {
                    _userDetails.value = userRepository.getUserDetails(it.uid)
                }
            }
        }
    }

    fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            _userDetails.value = userRepository.getUserDetails(userId)
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }
}



object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}