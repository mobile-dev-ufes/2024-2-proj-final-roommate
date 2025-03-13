package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.utils.statusEnum

class SignUpViewModel: ViewModel() {
    private var currentUser = MutableLiveData<UserModel>()
    private val userRepository = UserRepository()
    private var status: MutableLiveData<statusEnum> = userRepository.status

    fun isRegistered() : MutableLiveData<statusEnum>{
        return status
    }

    fun registerUser(user: UserModel){
        currentUser.value = user
        userRepository.create(user)
    }
}