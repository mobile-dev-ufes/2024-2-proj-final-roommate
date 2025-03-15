package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.FragmentCreateAd1Binding

class FragmentCreateAd1 : Fragment(R.layout.fragment_create_ad1) {
    private lateinit var binding: FragmentCreateAd1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd1Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad1ProceedBtn.setOnClickListener {
            val action = FragmentCreateAd1Directions.actionFragmentCreateAd1ToFragmentCreateAd2(adModelFromViewInfo())
            findNavController().navigate(action)
        }
    }

    private fun adModelFromViewInfo(): AdModel {
        return AdModel(
            title = binding.titleEt.text.toString(),
            rent_value = binding.rentEt.text.toString().toDouble(),
            cond_value = binding.condEt.text.toString().toDouble(),
            max_client = binding.clientsEt.text.toString().toInt(),
            description = binding.descriptionEt.text.toString(),
            suite_qtt = null,
            bedroom_qtt = null,
            area = null,
            local = null,
            groups = arrayOf()
        )
    }
}