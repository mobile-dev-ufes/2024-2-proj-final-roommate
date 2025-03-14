package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.statusEnum

class FeedViewModel: ViewModel() {
    private val adRepository = AdRepository()

    private var status : MutableLiveData<statusEnum> = adRepository.status
    private var adList = mutableListOf<AdModel>()

    fun getAdList() : MutableList<AdModel>{
        return adList ?: mutableListOf()
    }

    fun isOperationCompleted() : MutableLiveData<statusEnum>{
        return status
    }

    fun getAds() {
        adList = adRepository.getAllAds()
    }

}