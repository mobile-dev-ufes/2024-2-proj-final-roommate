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
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup1Binding
import java.time.LocalDate

/**
 * Fragment que exibe a primeira tela do fluxo de cadastro de usuário.
 *
 * Este fragmento permite que o usuário insira seu nome, sexo, telefone e data de nascimento.
 * Após preencher os campos, o usuário pode navegar para a próxima tela do fluxo de cadastro.
 *
 * @see [UserModel] Modelo de dados utilizado para armazenar as informações do usuário.
 */
class FragmentSignup1 : Fragment(R.layout.fragment_signup3), View.OnClickListener {
    private lateinit var binding: FragmentSignup1Binding

    private var user = UserModel()

    /**
     * Cria a view do fragmento e inicializa as dependências necessárias.
     *
     * Este método infla a view associada ao fragmento e configura o layout para a tela de cadastro.
     *
     * @param inflater O LayoutInflater que irá inflar a view.
     * @param container O contêiner pai para o qual a view será anexada.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     * @return A view inflada que será exibida no fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Infla a view associada ao fragmento.
        binding = FragmentSignup1Binding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Chamado após a view ser criada. Configura os listeners para os botões e interações.
     *
     * Neste método, os listeners de clique são configurados para os botões de navegação e o campo de data de nascimento.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o listener de clique para o botão de avançar.
        binding.signup1GoBtn.setOnClickListener(this)

        // Configura o listener de clique para o campo de data de nascimento.
        binding.birthdayTv.setOnClickListener(this)
    }

    /**
     * Método chamado quando qualquer visualização (view) é clicada.
     *
     * Este método verifica o clique no botão de navegação (para avançar para a próxima tela) ou no campo de data de nascimento.
     * Se o botão de navegação for clicado, os campos são verificados e as informações do usuário são salvas antes da navegação.
     * Se o campo de data de nascimento for clicado, um DatePicker é exibido para o usuário escolher sua data de nascimento.
     *
     * @param view A visualização que foi clicada.
     */
    override fun onClick(view: View) {
        // Verifica se o botão de navegação foi clicado e se os campos estão preenchidos.
        if (view.id == R.id.signup1_go_btn && checkFields()) {
            // Salva as informações inseridas no modelo de usuário.
            user.name = binding.nameEt.text.toString()
            user.sex = binding.sexSp.selectedItem.toString()
            user.phone = binding.phoneEt.masked

            // Navega para a próxima tela do fluxo de cadastro passando as informações do usuário.
            val action = FragmentSignup1Directions.actionFragmentSignup1ToFragmentSignup2(user)
            findNavController().navigate(action)

        // Verifica se o campo de data de nascimento foi clicado.
        } else if(view.id == R.id.birthday_tv){
            // Configura o listener para o DatePicker.
            val listener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // Salva a data de nascimento no modelo de usuário.
                    user.birthDate = LocalDate.of(year, month + 1, dayOfMonth)

                    // Exibe a data selecionada no campo de data de nascimento
                    val textData = "$dayOfMonth/${month + 1}/$year"
                    binding.birthdayTv.text = textData
                }

            // Obtém a data atual para exibir no DatePicker.
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), listener, cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    /**
     * Verifica se todos os campos obrigatórios estão preenchidos.
     *
     * Este método valida se os campos de nome, telefone e data de nascimento foram preenchidos corretamente.
     * Caso algum campo não esteja preenchido, um Toast é exibido informando ao usuário para preencher todos os campos.
     *
     * @return Retorna true se todos os campos obrigatórios estão preenchidos, caso contrário, retorna false.
     */
    private fun checkFields(): Boolean{
        if (binding.nameEt.text.isEmpty() ||
            binding.phoneEt.masked.isEmpty() ||
            binding.birthdayTv.text.isEmpty()){

            // Exibe uma mensagem de erro caso algum campo não tenha sido preenchido.
            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()

            return false
        }
        return true
    }
}