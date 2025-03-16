package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.utils.statusEnum

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private var currentUser = MutableLiveData<UserModel>()
    private var status = MutableLiveData<statusEnum>(statusEnum.NIL)

    fun isRegistered(): MutableLiveData<statusEnum> {
        return status
    }

    fun getCurrentUser(): UserModel{
        return currentUser.value ?: UserModel()
    }

    fun registerUser(user: UserModel) {
        currentUser.value = user
        userRepository.create(user, status)
    }

    fun getUser(userEmail: String) {
        userRepository.get(userEmail, status, currentUser)
    }

    fun addGroupToUser(userId: String, groupId: String) {
        userRepository.addGroupToUser(userId, groupId)
    }
}