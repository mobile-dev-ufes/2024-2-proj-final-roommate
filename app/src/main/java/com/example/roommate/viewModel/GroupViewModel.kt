package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.GroupRepository

class GroupViewModel : ViewModel() {
    private val userViewModel = UserViewModel()
    private val groupRepository = GroupRepository()

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    private val _members = MutableLiveData<List<UserModel>>()
    val members: LiveData<List<UserModel>> = _members

    private val _groupImageUrl = MutableLiveData<String>()
    val groupImageUrl: LiveData<String> = _groupImageUrl

    fun registerGroup(group: GroupModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        groupRepository.registerGroup(group, onSuccess, onFailure)
    }

    fun getMembersFromGroup(groupId: String) {
        groupRepository.getMembersFromGroup(groupId) { userList ->
            _members.postValue(userList)
        }
    }

    fun groupEntryLogic(groupId: String, userId: String) {
        groupRepository.addGroupMember(groupId, userId)
        userViewModel.addGroupToUser(userId, groupId)
    }

    fun loadGroupImage(groupId: String) {
        groupRepository.getGroupImage(
            groupId = groupId,
            onSuccess = { url -> _groupImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("groupViewModel", "Failed to get image URL", exception) }
        )
    }
}
