package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentAdvertisementBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FragmentAdvertisement : Fragment(R.layout.fragment_advertisement) {
    private lateinit var binding: FragmentAdvertisementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAdvertisementBinding.inflate(inflater, container, false)

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




//        binding.adGroupBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_fragmentAdvertisement_to_fragmentInterestedGroups)
//        }
    }
}