package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.AuthRepository
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.utils.statusEnum

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private var status = MutableLiveData<authStatusEnum>(authStatusEnum.NIL)

    fun isRegistered(): MutableLiveData<authStatusEnum> {
        return status
    }

    fun registerUser(email: String, pass: String){
        authRepository.registerUser(email, pass, status)
    }
}