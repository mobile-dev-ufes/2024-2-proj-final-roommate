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
import com.example.roommate.databinding.FragmentMyAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.viewModel.MyAdsViewModel


class FragmentMyAds : Fragment(R.layout.fragment_my_ads) {
    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapter: ListAdAdapter
    private lateinit var myAdsVM: MyAdsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        myAdsVM = ViewModelProvider(this)[MyAdsViewModel::class.java]

        binding = FragmentMyAdsBinding.inflate(inflater, container, false)

        adapter = ListAdAdapter{adModel ->
            val action = FragmentMyAdsDirections.actionFragmentMyAdsToFragmentAdvertisement(adModel)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        myAdsVM.getAds()

        binding.myAdsBtn.setOnClickListener {
            val action = FragmentMyAdsDirections.actionFragmentMyAdsToFragmentCreateAd1(1)
            findNavController().navigate(action)
        }

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }

    private fun setObserver(){
        myAdsVM.getAdList().observe(viewLifecycleOwner){ adList ->
            adapter.updateAdList(adList)
        }
    }
}