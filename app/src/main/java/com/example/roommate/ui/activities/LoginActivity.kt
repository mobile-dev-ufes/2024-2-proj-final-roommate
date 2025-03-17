package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.ActivityLoginBinding
import com.example.roommate.data.repository.AuthRepository
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.utils.statusEnum
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.AuthViewModel
import com.example.roommate.viewModel.UserViewModel

/**
 * Activity responsável pelo processo de login da aplicação.
 *
 * A [LoginActivity] gerencia a interface de login, valida os campos de entrada e comunica-se com a
 *  camada de ViewModel
 *  para realizar a autenticação do usuário. Caso o login seja bem-sucedido, o usuário é
 *  redirecionado para a tela principal.
 *
 * - Utiliza [AuthViewModel] para autenticação.
 * - Utiliza [UserViewModel] para obter as informações do usuário após a autenticação.
 * - Exibe mensagens de erro via [Toast] para informar ao usuário caso haja problemas com a
 *  autenticação ou a conexão.
 */
class LoginActivity : AppCompatActivity() {
    // Binding da Activity para acessar os elementos da interface
    private lateinit var binding: ActivityLoginBinding

    // ViewModels para autenticação e informações do usuário
    private lateinit var userVM: UserViewModel
    private lateinit var authVM: AuthViewModel

    /**
     * Método chamado quando a Activity é criada.
     *
     * Inicializa o binding da view, configura os ViewModels e define observadores para o estado da
     *  autenticação e do usuário.
     * Também configura os listeners para os botões de login e cadastro.
     *
     * @param savedInstanceState Estado da Activity salvo anteriormente (se houver).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Exibe a tela de splash enquanto a Activity está sendo carregada
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Inicializa o binding da Activity
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa os ViewModels para autenticação e usuário
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        authVM = ViewModelProvider(this)[AuthViewModel::class.java]

        // Configura observadores para autenticação e recuperação de dados do usuário
        setObserver()

        // Configura o clique no link de cadastro
        binding.signUpTv.setOnClickListener({
            startActivity((Intent(this, SignUpActivity::class.java)))
        })

        // Configura o clique no botão de login
        binding.signInBtn.setOnClickListener({
            if (checkFields()) {
                val email = binding.emailEt.text.toString()
                val pass = binding.passEt.text.toString()

                // Realiza a autenticação do usuário com as credenciais fornecidas
                authVM.authenticateUser(email, pass)
            }
        })
    }

    /**
     * Navega para a tela principal (HomeActivity) após o login bem-sucedido.
     */
    private fun navigate(){
        startActivity(Intent(this, HomeActivity::class.java))
        // finish()
    }

    /**
     * Configura os observadores para monitorar o status da autenticação e a recuperação dos dados
     *  do usuário.
     */
    private fun setObserver() {
        // Observa o status da autenticação do usuário
        authVM.isRegistered().observe(this){ status ->
            when(status){
                // Sucesso na autenticação, buscar dados do usuário
                authStatusEnum.SUCCESS ->{
                    userVM.getUser(binding.emailEt.text.toString())
                }
                // Erro de credenciais inválidas
                authStatusEnum.INVALID_CREDENTIAL ->{
                    Toast.makeText(this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show()
                }
                // Erro de rede
                authStatusEnum.NETWORK ->{
                    Toast.makeText(this, "Verfique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
                }
                // Falha geral na autenticação
                authStatusEnum.FAIL ->{
                    Toast.makeText(this, "Erro ao realizar o login.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        // Observa o status da recuperação dos dados do usuário
        userVM.isRegistered().observe(this) { status ->
            when (status) {
                // Sucesso na recuperação dos dados do usuário
                statusEnum.SUCCESS -> {
                    val currentUser = userVM.getCurrentUser()

                    // Atualiza os dados do usuário no gerenciador global (userManager)
                    userManager.user = UserModel(
                        email = currentUser.email,
                        name = currentUser.name,
                        bio = currentUser.bio,
                        sex = currentUser.sex,
                        phone = currentUser.phone,
                        birthDate = currentUser.birthDate,
                        photo_uri = currentUser.photo_uri
                    )
                    navigate()  // Navega para a tela principal
                }
                // Falha ao encontrar o registro do usuário
                statusEnum.FAIL -> {
                    Toast.makeText(
                        this,
                        "Registro do usuário não localizado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> UInt
            }
        }
    }

    /**
     * Verifica se os campos de email e senha foram preenchidos.
     *
     * @return Booleano indicando se os campos estão válidos ou não.
     */
    private fun checkFields(): Boolean {
        if (binding.emailEt.text.isEmpty() || binding.passEt.text.isEmpty()) {
            Toast.makeText(
                this,
                "Preencha todos os campos",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }
        return true
    }
}