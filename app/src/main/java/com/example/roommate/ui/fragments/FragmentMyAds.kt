package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.FragmentMyAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter


class FragmentMyAds : Fragment(R.layout.fragment_my_ads) {
    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapter: ListAdAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyAdsBinding.inflate(inflater, container, false)
        adapter = ListAdAdapter{
            findNavController().navigate(R.id.action_fragmentMyAds_to_fragmentAdvertisement)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myAdsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMyAds_to_fragmentCreateAd1)
        }

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Apenas para testar o Recycle View
        adapter.updateAdList(mutableListOf( AdModel("TESTE3", "Bela Aurora/ Cariacica", 250.0)))
        adapter.insertAdList(AdModel("TESTE2", "Bela Aurora/ Cariacica", 500.0))
        adapter.insertAdList(AdModel("TESTE1", "Bela Aurora/ Cariacica", 750.0))
    }
}