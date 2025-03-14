package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.statusEnum

class FeedViewModel: ViewModel() {
    private val adRepository = AdRepository()
    private var adList = MutableLiveData<MutableList<AdModel>>()

    fun getAdList() : MutableLiveData<MutableList<AdModel>>{
        return adList
    }

    fun getAds() {
        adRepository.getAllAds(adList)
    }

}