package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.DialogCreateGroupBinding
import com.example.roommate.ui.viewModels.GroupViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class DialogCreateGroup: DialogFragment(R.layout.dialog_create_group) {
    private lateinit var binding: DialogCreateGroupBinding
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogCreateGroupBinding.inflate(inflater, container, false)
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lixo de código apenas pq não foi passado a referência por navegação
        val db = FirebaseFirestore.getInstance()
        val advertisementId = "D3KsuvpM8BrDlg8MD5tP"
        val advertisementRef: DocumentReference = db.collection("group").document(advertisementId)

        binding.createGroupBtn.setOnClickListener {
            groupViewModel.registerGroup(
                GroupModel(
                    binding.createGroupNameTv.text.toString(),
                    binding.createGroupDescriptionTv.text.toString(),
                    advertisementRef
                )
            )

            // Navigate and clean the stack until FragmentInterestedGroups
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentInterestedGroups, true) // Clears back stack up to fragmentMyAds
                .build()

            findNavController().navigate(
                R.id.action_dialogCreateGroup_to_fragmentInterestedGroups,
                null,
                navOptions
            )
        }
    }
}