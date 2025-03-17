package com.example.roommate.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.roommate.R
import com.example.roommate.databinding.ActivityHomeBinding

/**
 * Activity principal que representa a tela inicial da aplicação.
 *
 * A [HomeActivity] gerencia a navegação entre os diferentes fragmentos da aplicação e configura a interface do usuário
 * com a toolbar e o menu de navegação inferior (BottomNavigationView).
 *
 * - A Activity utiliza o sistema de navegação do Android com o [NavController] e [NavHostFragment].
 * - A toolbar é configurada para interagir com o [NavController] e exibir o menu de navegação no topo.
 * - O BottomNavigationView é exibido ou ocultado dependendo do fragmento atual exibido.
 */
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    /**
     * Método chamado na criação da Activity.
     *
     * Inicializa o binding da view, configura a interface do usuário e chama a função de configuração da toolbar.
     *
     * @param savedInstanceState Estado da Activity salvo anteriormente (se houver).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
    }

    /**
     * Método que configura a toolbar e o menu de navegação inferior.
     *
     * - Configura o [NavController] para interagir com a [Toolbar] e [BottomNavigationView].
     * - Configura a exibição ou ocultação do BottomNavigationView baseado no fragmento atual.
     */
    private fun setToolbar() {
        // Configura a toolbar com o NavController e AppBarConfiguration
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragment_container_home) as NavHostFragment

        val navController = navHostFrag.navController
        binding.bottomNavMenu.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)

        // Adiciona um listener para mostrar ou ocultar o BottomNavigationView conforme o fragmento atual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = destination.id == R.id.fragmentAds ||
                    destination.id == R.id.fragmentMyProfile ||
                    destination.id == R.id.fragmentMyGroups

            binding.bottomNavMenu.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }
    }
}