package com.example.roommate.viewModel

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

    fun registerGroup(group: GroupModel) {
        groupRepository.registerGroup(group)
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
}
