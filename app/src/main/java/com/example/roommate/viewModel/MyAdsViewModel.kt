package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.userManager

class MyAdsViewModel: ViewModel() {
    private val adRepository = AdRepository()

    private var adList = MutableLiveData<MutableList<AdModel>>()

    private val _adImageUrl = MutableLiveData<String>()
    val adImageUrl: LiveData<String> = _adImageUrl

    fun getAdList() : MutableLiveData<MutableList<AdModel>>{
        return adList
    }

    fun getAds() {
        adRepository.getAdsByUser(userManager.user.email!!, adList)
    }

    fun loadAdImage(adId: String) {
        adRepository.getAdImage(
            adId = adId,
            onSuccess = { url -> _adImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("adViewModel", "Failed to get image URL", exception) }
        )
    }
}