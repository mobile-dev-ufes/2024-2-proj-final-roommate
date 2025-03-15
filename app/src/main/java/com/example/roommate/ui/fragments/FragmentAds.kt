package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.viewModel.FeedViewModel

class FragmentAds : Fragment(R.layout.fragment_ads) {
    private lateinit var binding: FragmentAdsBinding
    private lateinit var adapter: ListAdAdapter

    private lateinit var adsVM: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAdsBinding.inflate(inflater, container, false)

        adapter = ListAdAdapter { adModel ->
            val action = FragmentAdsDirections.actionFragmentAdsToFragmentAdvertisement(adModel)
            findNavController().navigate(action)
        }

        adsVM = ViewModelProvider(this)[FeedViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        adsVM.getAds()

        binding.createAdBtn.setOnClickListener {
            val action = FragmentAdsDirections.actionFragmentAdsToFragmentCreateAd1(0)
            findNavController().navigate(action)
        }

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }

    private fun setObserver(){
        adsVM.getAdList().observe(viewLifecycleOwner) { adList ->
            adapter.updateAdList(adList)
        }
    }
}