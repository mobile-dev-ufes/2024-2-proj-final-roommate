package com.example.roommate.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentAdvertisementBinding
import com.example.roommate.ui.adapters.ListBenefitsAdapter

class FragmentAdvertisement : Fragment(R.layout.fragment_advertisement) {
    private lateinit var binding: FragmentAdvertisementBinding
    private lateinit var adapter: ListBenefitsAdapter

    private val args: FragmentAdvertisementArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAdvertisementBinding.inflate(inflater, container, false)
        adapter = ListBenefitsAdapter()

        bindInfo()

        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hardcoded
        val advertisementId = "D3KsuvpM8BrDlg8MD5tP"
        val userId = "leticia@email.com"

        binding.adGroupBtn.setOnClickListener {
            val action =
                FragmentAdvertisementDirections.actionFragmentAdvertisementToFragmentInterestedGroups(
                        advertisementId,
                        userId
                    )
            findNavController().navigate(action)
        }

        binding.gMapsImg.setOnClickListener {
            goToMap()
        }

        binding.recycleListBenefits.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleListBenefits.adapter = adapter

        adapter.updateBenefitList(args.adInfo.getBenefitsList())
    }

    private fun bindInfo() {
        binding.adTitle.text = args.adInfo.title
        binding.localTv.text = args.adInfo.localToString()
        binding.adDescriptionTv.text = args.adInfo.description
        binding.adAddressTv.text = args.adInfo.localCompleteToString()

        binding.bedroomTv.text = args.adInfo.bedroom_qtt.toString()
        binding.suitesTv.text = args.adInfo.suite_qtt.toString()
        binding.areaTv.text = args.adInfo.area.toString()
        binding.clientsTv.text = args.adInfo.max_client.toString()
        binding.parkingTv.text = args.adInfo.parking_qtt.toString()
    }

    private fun goToMap() {
        val address =
            "${args.adInfo.local?.street}, ${args.adInfo.local?.number}, ${args.adInfo.local?.nb}, ${args.adInfo.local?.city}, ${args.adInfo.local?.state}"

        val webUri = Uri.parse("https://www.google.com/maps/search/$address")

        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        startActivity(webIntent)
    }
}