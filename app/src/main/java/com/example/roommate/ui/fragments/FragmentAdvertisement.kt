package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentAdvertisementBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.ui.adapters.ListBenefitsAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FragmentAdvertisement : Fragment(R.layout.fragment_advertisement) {
    private lateinit var binding: FragmentAdvertisementBinding
    private lateinit var adapter: ListBenefitsAdapter

    private val args: FragmentAdvertisementArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAdvertisementBinding.inflate(inflater, container, false)
        adapter = ListBenefitsAdapter()

        bindInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hardcoded
        val advertisementId = "D3KsuvpM8BrDlg8MD5tP"
        val userId = "daniel@gmail.com"

        binding.adGroupBtn.setOnClickListener {
            val action = FragmentAdvertisementDirections
                .actionFragmentAdvertisementToFragmentInterestedGroups(advertisementId, userId)
            findNavController().navigate(action)
        }

        binding.recycleListBenefits.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleListBenefits.adapter = adapter

        adapter.updateBenefitList(mutableListOf("Deu", "certo", "galera"))
    }

    private fun bindInfo(){
        binding.adTitle.text = args.adInfo.title
        binding.localTv.text = args.adInfo.localToString()
        binding.adDescriptionTv.text = args.adInfo.description
        binding.adAddressTv.text = args.adInfo.localCompleteToString()
    }
}