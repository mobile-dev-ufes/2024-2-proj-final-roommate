package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.databinding.FragmentCreateAd2Binding

class FragmentCreateAd2 : Fragment(R.layout.fragment_create_ad2) {
    private lateinit var binding: FragmentCreateAd2Binding

    private val args: FragmentCreateAd2Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad2ProceedBtn.setOnClickListener {
            val action = FragmentCreateAd2Directions.actionFragmentCreateAd2ToFragmentCreateAd3(adModelFromViewInfo())
            findNavController().navigate(action)
        }
    }

    private fun adModelFromViewInfo(): AdModel {
        val address = Address(
            cep = binding.cepEt.text.toString(),
            number = binding.numberEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            street = binding.streetEt.text.toString(),
            nb = binding.nbEt.text.toString(),
            city = binding.cityEt.text.toString(),
            state = binding.stateEt.text.toString(),
        )

        return AdModel(
            owner = args.adInfo.owner,
            title = args.adInfo.title,
            rent_value = args.adInfo.rent_value,
            cond_value = args.adInfo.cond_value,
            max_client = args.adInfo.max_client,
            description = args.adInfo.description,
            local = address,
            suite_qtt = null,
            bedroom_qtt = null,
            area = null,
            groups = arrayOf()
        )
    }
}