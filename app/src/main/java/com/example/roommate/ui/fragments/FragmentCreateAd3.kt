package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.databinding.FragmentCreateAd3Binding
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.CreateAdViewModel

class FragmentCreateAd3 : Fragment(R.layout.fragment_create_ad3) {
    private lateinit var binding: FragmentCreateAd3Binding
    private lateinit var createAdVM: CreateAdViewModel

    private val args: FragmentCreateAd3Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd3Binding.inflate(inflater, container, false)
        createAdVM = ViewModelProvider(this)[CreateAdViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        binding.ad3FinishRegistration.setOnClickListener {
            createAdVM.registerAd(adModelFromViewInfo())
        }
    }

    private fun adModelFromViewInfo(): AdModel {
        val benefits = mapOf(
            "ladies" to binding.ladiesCb.isChecked,
            "garage" to binding.garageCb.isChecked,
            "internet" to binding.internetCb.isChecked,
            "hotWater" to binding.hotWaterCb.isChecked,
            "ar" to binding.arCb.isChecked,
            "pool" to binding.poolCb.isChecked,
            "pet" to binding.petCb.isChecked,
            "security" to binding.securityCb.isChecked)

        return AdModel(
            title = args.adInfo.title,
            rent_value = args.adInfo.rent_value,
            cond_value = args.adInfo.cond_value,
            max_client = args.adInfo.max_client,
            description = args.adInfo.description,
            local = args.adInfo.local,
            bedroom_qtt = binding.bedroomEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            suite_qtt = binding.suitesEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            area = binding.areaEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0,
            benefits = benefits,
            groups = arrayOf()
        )
    }

    private fun navigate(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.fragmentMyAds, true) // Clears back stack up to fragmentMyAds
            .build()

        findNavController().navigate(
            R.id.action_fragmentCreateAd3_to_fragmentMyAds,
            null,
            navOptions
        )
    }

    private fun setObserver() {
        createAdVM.isRegistered().observe(viewLifecycleOwner) { status ->
            when (status) {
                statusEnum.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        "Anúncio registrado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }

                statusEnum.FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro! Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }

                else -> UInt
            }
        }
    }
}