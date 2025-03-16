package com.example.roommate.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.FragmentCreateAd1Binding
import com.example.roommate.utils.userManager

class FragmentCreateAd1 : Fragment(R.layout.fragment_create_ad1) {
    private lateinit var binding: FragmentCreateAd1Binding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    private val args: FragmentCreateAd1Args by navArgs()
    private var ad = AdModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    manageImage()
                    // Toast.makeText(requireContext(), "Permissão concedida com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permissão negada. Não será possível selecionar imagens.", Toast.LENGTH_SHORT).show()
                }
            }

        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                ad.photos.add(it.toString())
                binding.qttPhotosTv.text = ad.photos.size.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd1Binding.inflate(inflater, container, false)
        binding.qttPhotosTv.text = "0"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad1ProceedBtn.setOnClickListener {
            updateAdFromViewInfo()
            val action = FragmentCreateAd1Directions.actionFragmentCreateAd1ToFragmentCreateAd2(ad, args.route)
            findNavController().navigate(action)
        }
        binding.addPhotosL.setOnClickListener{
            checkAccessPermission()
        }
    }

    private fun updateAdFromViewInfo(){
        ad.owner = userManager.user.email
        ad.title = binding.titleEt.text.toString()
        ad.rent_value =binding.rentEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
        ad.cond_value = binding.condEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
        ad.max_client = binding.clientsEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0
        ad.description = binding.descriptionEt.text.toString()
        ad.suite_qtt = null
        ad.bedroom_qtt = null
        ad.parking_qtt = null
        ad.area = null
        ad.local = null
        ad.groups = arrayOf()
    }

    private fun checkAccessPermission(){
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                manageImage()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_MEDIA_LOCATION)
            }
        }
    }

    private fun manageImage(){
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}