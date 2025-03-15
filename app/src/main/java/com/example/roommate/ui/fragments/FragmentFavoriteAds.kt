package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentFavoriteAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter

class FragmentFavoriteAds : Fragment(R.layout.fragment_favorite_ads) {
    private lateinit var binding: FragmentFavoriteAdsBinding
    private lateinit var adapter: ListAdAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFavoriteAdsBinding.inflate(inflater, container, false)
        adapter = ListAdAdapter {
            findNavController().navigate(R.id.action_fragmentFavoriteAds_to_fragmentAdvertisement)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }
}