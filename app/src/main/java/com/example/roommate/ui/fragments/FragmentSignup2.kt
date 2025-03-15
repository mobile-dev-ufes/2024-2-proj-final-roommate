package com.example.roommate.ui.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup2Binding
import java.time.LocalDate

class FragmentSignup2 : Fragment(R.layout.fragment_signup1), View.OnClickListener {
    private lateinit var binding: FragmentSignup2Binding

    private val args: FragmentSignup2Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSignup2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup2GoBtn.setOnClickListener(this)
        binding.birthdayTv.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.signup2_go_btn && checkFields()) {
            val action = FragmentSignup2Directions.actionFragmentSignup2ToFragmentSignup3(
                UserModel(
                    email = args.userInfo.email,
                    name = binding.nameEt.text.toString(),
                    bio = null,
                    sex = binding.sexSp.selectedItem.toString(),
                    phone = binding.phoneEt.masked,
                    birthDate = args.userInfo.birthDate,
                    photo_uri = null
                )
            )
            findNavController().navigate(action)

        } else if(view.id == R.id.birthday_tv){
            val listener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    args.userInfo.birthDate = LocalDate.of(year, month + 1, dayOfMonth)

                    val textData = "$dayOfMonth/${month + 1}/$year"
                    binding.birthdayTv.text = textData
                }

            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), listener, cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun checkFields(): Boolean{
        if (binding.nameEt.text.isEmpty() ||
            binding.phoneEt.masked.isEmpty() ||
            binding.birthdayTv.text.isEmpty()){

            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()

            return false
        }
        return true
    }
}