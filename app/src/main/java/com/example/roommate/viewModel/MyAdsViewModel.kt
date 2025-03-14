package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.userManager

class MyAdsViewModel: ViewModel() {
    private val adRepository = AdRepository()

    private var adList = MutableLiveData<MutableList<AdModel>>()

    fun getAdList() : MutableLiveData<MutableList<AdModel>>{
        return adList
    }

    fun getAds() {
        adRepository.getAdsByUser(userManager.user.email!!, adList)
    }
}