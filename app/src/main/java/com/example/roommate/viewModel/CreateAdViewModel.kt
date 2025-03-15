package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.statusEnum

class CreateAdViewModel: ViewModel() {
    private val adRepository = AdRepository()

    private var status = MutableLiveData<statusEnum>()

    fun isRegistered(): MutableLiveData<statusEnum> {
        return status
    }

    fun registerAd(user: AdModel) {
        adRepository.create(user, status)
    }

}