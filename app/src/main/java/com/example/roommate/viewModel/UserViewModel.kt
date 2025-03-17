package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.utils.statusEnum

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private var currentUser = MutableLiveData<UserModel>()
    private var status = MutableLiveData<statusEnum>(statusEnum.NIL)

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> = _profileImageUrl

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

    fun getGroupsForUser(userId: String) {
        userRepository.getGroupsForUser(userId) { userList ->
            _groups.postValue(userList)
        }
    }
    fun addGroupToUser(userId: String, groupId: String) {
        userRepository.addGroupToUser(userId, groupId)
    }


    fun loadProfileImage(userId: String) {
        userRepository.getProfileImage(
            userId = userId,
            onSuccess = { url -> _profileImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("ProfileViewModel", "Failed to get image URL", exception) }
        )
    }

    fun getProfileImage(userId: String, onImageLoaded: (String) -> Unit) {
        userRepository.getProfileImage(
            userId = userId,
            onSuccess = { url -> onImageLoaded(url) },
            onFailure = { exception ->
                Log.e("UserViewModel", "Failed to get image URL for $userId", exception)
                onImageLoaded("") // Default to empty string to prevent errors
            }
        )
    }

}