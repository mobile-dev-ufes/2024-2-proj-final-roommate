package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.viewModel.MyAdsViewModel

class FragmentAds : Fragment(R.layout.fragment_ads) {
    private lateinit var binding: FragmentAdsBinding
    private lateinit var adapter: ListAdAdapter

    private lateinit var adsVM: MyAdsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAdsBinding.inflate(inflater, container, false)

        adsVM = ViewModelProvider(this)[MyAdsViewModel::class.java]

        adapter = ListAdAdapter(
            onClickItem = { adModel ->
                val action = FragmentAdsDirections.actionFragmentAdsToFragmentAdvertisement(adModel)
                findNavController().navigate(action)
            },
            onClickButton = { ad ->
                Toast.makeText(requireContext(), "Clicou no botão do anúncio: ${ad.title}", Toast.LENGTH_SHORT).show()
            },
            adViewModel = adsVM
        )


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