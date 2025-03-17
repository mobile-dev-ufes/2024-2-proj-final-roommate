package com.example.roommate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup1Binding
import com.example.roommate.databinding.FragmentSignup3Binding
import com.example.roommate.ui.activities.LoginActivity
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.AuthViewModel
import com.example.roommate.viewModel.UserViewModel

/**
 * Fragmento responsável pela terceira e última etapa do processo de cadastro de usuário.
 *
 * O [FragmentSignup3] permite que o usuário forneça um e-mail e senha para finalizar o cadastro.
 * O fragmento lida com a verificação de campos, a validação de senha e e-mail, além de monitorar o
 * status do processo de registro através de observadores vinculados aos [ViewModels].
 *
 * Após o sucesso do registro, o usuário é redirecionado para a tela de login.
 */
class FragmentSignup3 : Fragment(R.layout.fragment_signup3), View.OnClickListener {
    // Binding da view do fragmento, utilizado para acessar os elementos da interface
    private lateinit var binding: FragmentSignup3Binding

    // ViewModels responsáveis pelo gerenciamento do cadastro de usuário e autenticação
    private lateinit var signUpVM: UserViewModel
    private lateinit var authVM: AuthViewModel

    // Argumentos passados pelo fragmento anterior contendo as informações do usuário
    private val args: FragmentSignup3Args by navArgs()

    /**
     * Método chamado para inflar a view do fragmento e inicializar o ViewModel.
     *
     * Este método infla o layout do fragmento e retorna a raiz da view.
     *
     * @param inflater O objeto [LayoutInflater] usado para inflar a view.
     * @param container O container no qual a view será colocada.
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     * @return A raiz da view inflada.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o binding para acessar os elementos da interface do fragmento
        binding = FragmentSignup3Binding.inflate(inflater, container, false)

        // Inicializa os ViewModels
        signUpVM = ViewModelProvider(this)[UserViewModel::class.java]
        authVM = ViewModelProvider(this)[AuthViewModel::class.java]

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para interação.
     *
     * Este método configura os observadores para acompanhar as mudanças de estado dos ViewModels
     * e inicializa os listeners de clique para o botão de finalização do cadastro.
     *
     * @param view A view recém-criada do fragmento.
     * @param savedInstanceState O estado salvo da instância do fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura os observadores para o estado dos processos de autenticação e registro
        setObserver()

        // Configura o listener para o botão de finalização do cadastro
        binding.finishSignUp.setOnClickListener(this)
    }

    /**
     * Método de callback para os cliques nos botões da interface.
     *
     * - Ao clicar no botão de finalização de cadastro, os campos de e-mail e senha são verificados.
     *   Se todos os campos estiverem corretos, o processo de registro do usuário é iniciado.
     *
     * @param view A view que foi clicada.
     */
    override fun onClick(view: View) {
        // Verifica se o botão de finalizar cadastro foi clicado
        if (view.id == R.id.finish_sign_up && checkFields()) {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            // Atribui o e-mail fornecido ao modelo de usuário
            args.userInfo.email = email

            // Registra o usuário no sistema com o e-mail e a senha fornecidos
            authVM.registerUser(email, pass)
        }
    }

    /**
     * Método que configura os observadores para os estados de autenticação e registro do usuário.
     * - Observa o estado do processo de registro no [authVM] para reagir ao sucesso, falha, ou erros de validação.
     * - Observa o estado do processo de registro no [signUpVM] para confirmar se o cadastro foi bem-sucedido.
     */
    private fun setObserver(){
        // Observa o estado de autenticação (registro do usuário)
        authVM.isRegistered().observe(viewLifecycleOwner){ status ->
            when(status){
                authStatusEnum.SUCCESS ->{
                    // Se o registro for bem-sucedido, registra o usuário no banco de dados
                    signUpVM.registerUser(args.userInfo)
                }
                // Emite avisos para verificações
                authStatusEnum.WEAK_PASS ->{
                    Toast.makeText(context, "Senha fraca! A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.COLLISION ->{
                    Toast.makeText(context, "Usuário já cadastrado!", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.NETWORK ->{
                    Toast.makeText(context, "Verfique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.FAIL ->{
                    Toast.makeText(context, "Verifique se o e-mail tem formato válido.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        // Observa o estado de registro no banco de dados
        signUpVM.isRegistered().observe(viewLifecycleOwner) { status ->
            when (status) {
                // Se o cadastro for bem-sucedido, exibe uma mensagem e navega para a tela de login
                statusEnum.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }
                // Se o cadastro falhar, exibe uma mensagem de erro
                statusEnum.FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro! Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }
                // Se houver um erro ao salvar a imagem de perfil, exibe uma mensagem de erro
                statusEnum.FAIL_IMG -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro ao salvar a imagem.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }

                else -> UInt
            }
        }
    }

    /**
     * Método que verifica se todos os campos obrigatórios estão preenchidos corretamente.
     *
     * - Verifica se os campos de e-mail e senha estão preenchidos.
     * - Verifica se os e-mails e as senhas fornecidos coincidem.
     * - Exibe mensagens de erro caso algum campo esteja incorreto.
     *
     * @return Retorna true se todos os campos estiverem preenchidos corretamente, caso contrário, false.
     */
    private fun checkFields(): Boolean{
        if (binding.emailEt.text.isEmpty() ||
            binding.confirmEmailEt.text.isEmpty() ||
            binding.passEt.text.isEmpty() ||
            binding.confirmPassEt.text.isEmpty()) {

            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()

            return false
        }
        if (binding.emailEt.text.toString() != binding.confirmEmailEt.text.toString()){
            Toast.makeText(context, "Os e-mails não coincidem.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.passEt.text.toString() != binding.confirmPassEt.text.toString()){
            Toast.makeText(context, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    /**
     * Método responsável por navegar para a tela de login após a conclusão do cadastro.
     */
    private fun navigate(){
        // Inicia a atividade de login e finaliza a atividade atual
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}