package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.DialogCreateGroupBinding
import com.example.roommate.viewModel.GroupViewModel

class DialogCreateGroup: DialogFragment(R.layout.dialog_create_group) {
    private lateinit var binding: DialogCreateGroupBinding
    private lateinit var groupViewModel: GroupViewModel
    private val args: DialogCreateGroupArgs by navArgs()

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

        binding.createGroupBtn.setOnClickListener {
            groupViewModel.registerGroup(
                GroupModel(
                    id = "",
                    name = binding.createGroupNameTv.text.toString(),
                    description = binding.createGroupDescriptionTv.text.toString(),
                    advertisementId = args.advertisementId,
                    qttMembers = 0,
                    isPrivate = binding.switchGroup.isChecked
                )
            )

            // Navigate and clean the stack until FragmentInterestedGroups
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentInterestedGroups, true) // Clears back stack up to fragmentMyAds
                .build()

            val action = DialogCreateGroupDirections
                .actionDialogCreateGroupToFragmentInterestedGroups(args.advertisementId)

            findNavController().navigate(action, navOptions)

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // This removes the default dark background
    }
}