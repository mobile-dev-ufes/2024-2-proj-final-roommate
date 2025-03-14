package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.utils.statusEnum

class UserViewModel: ViewModel() {
    private var currentUser = MutableLiveData<UserModel>()
    private val userRepository = UserRepository()
    private var status = MutableLiveData<statusEnum>(statusEnum.NIL)

    fun isRegistered() : MutableLiveData<statusEnum>{
        return status
    }

    fun registerUser(user: UserModel){
        currentUser.value = user
        userRepository.create(user).observeForever { result ->
            status.value = result
        }
    }

//    fun getUser(userEmail: String): UserModel{
//        return userRepository.get(userEmail)
//    }
}