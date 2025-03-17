package com.example.roommate.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentInterestedGroupsBinding
import com.example.roommate.ui.adapters.ListInterestedGroupAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.atomic.AtomicInteger

/**
 * Fragmento responsável por exibir os grupos interessados em um anúncio específico.
 *
 * O [FragmentInterestedGroups] é responsável por recuperar e exibir uma lista de grupos interessados
 * em um determinado anúncio. Ele permite que o usuário visualize os grupos existentes e crie novos
 * grupos.
 * O fragmento utiliza um [RecyclerView] para exibir os grupos e navega para o [FragmentGroup]
 * ao clicar em um grupo. Além disso, o fragmento oferece a opção de criar um novo grupo
 * relacionado ao anúncio.
 */
class FragmentInterestedGroups : Fragment(R.layout.fragment_interested_groups) {

    // Binding da view do fragmento
    private lateinit var binding: FragmentInterestedGroupsBinding

    // Adaptador utilizado para exibir a lista de grupos no RecyclerView
    private lateinit var adapter: ListInterestedGroupAdapter

    // Argumentos passados para o fragmento via Safe Args, contendo o ID do anúncio
    private val args: FragmentInterestedGroupsArgs by navArgs()

    /**
     * Método chamado para inflar a view do fragmento e inicializar o adaptador e os componentes
     * necessários.
     *
     * @param inflater O objeto LayoutInflater usado para inflar a view.
     * @param container O container onde a view será colocada.
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     * @return A raiz da view inflada.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o binding para acessar as views do fragmento
        binding = FragmentInterestedGroupsBinding.inflate(inflater, container, false)

        // Inicializa o adaptador e configura a navegação ao clicar em um grupo
        adapter = ListInterestedGroupAdapter { group ->
            val action = FragmentInterestedGroupsDirections
                .actionFragmentInterestedGroupsToFragmentGroup(group) // Passa o grupo para o próximo fragmento
            findNavController().navigate(action)
        }

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para ser interagida.
     *
     * - Configura o RecyclerView e define o adaptador.
     * - Recupera a lista de grupos interessados com base no ID do anúncio.
     * - Configura a navegação para a criação de um novo grupo.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo do fragmento, caso haja.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView com o LayoutManager e o adaptador
        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Obtém o ID do anúncio passado via Safe Args
        val advertisementId = args.advertisementId
        // Recupera os grupos interessados a partir do ID do anúncio
        getGroupsFromAdvertisementId(advertisementId)

        // Configura o botão de criação de grupo
        binding.createGroupBtn.setOnClickListener {
            val action = FragmentInterestedGroupsDirections
                .actionFragmentInterestedGroupsToDialogCreateGroup(advertisementId)
            findNavController().navigate(action)
        }
    }

    /**
     * Método responsável por recuperar os grupos interessados relacionados ao ID de um anúncio.
     * Faz uma consulta ao Firestore para obter os grupos associados ao anúncio e atualiza a lista
     * de grupos.
     *
     * @param advertisementId O ID do anúncio que será utilizado para buscar os grupos interessados.
     */
    private fun getGroupsFromAdvertisementId(advertisementId: String) {
        val db = FirebaseFirestore.getInstance()

        // Referência ao documento do anúncio
        val advertisementRef = db.collection("advertisement").document(advertisementId)

        // Recupera o documento do anúncio para obter o campo "groups"
        advertisementRef.get()
            .addOnSuccessListener { advertisementDocument ->
                val groupRefs = (advertisementDocument.get("groups") as? List<*>)?.mapNotNull { it as? DocumentReference }

                if (groupRefs.isNullOrEmpty()) {
                    Log.w("Firebase", "No valid group references found for advertisement ID: $advertisementId")
                    return@addOnSuccessListener
                }

                Log.d("Firebase", "Retrieved group references: $groupRefs")

                val groupsList = mutableListOf<GroupModel>()
                val remainingRequests = AtomicInteger(groupRefs.size)

                // Itera sobre as referências dos grupos e recupera os dados de cada grupo
                groupRefs.forEach { groupRef ->
                    groupRef.get()
                        .addOnSuccessListener { groupDocument ->
                            val groupName = groupDocument.getString("name").orEmpty()
                            val groupDescription = groupDocument.getString("description").orEmpty()
                            val qttMembers = (groupDocument.get("qttMembers") as? Long)?.toInt() ?: 0
                            val isPrivate = groupDocument.getBoolean("isPrivate") ?: false
                            val photoUri = groupDocument.getString("photoUri") ?: ""

                            // Cria o objeto GroupModel com os dados recuperados
                            val group = GroupModel(
                                id = groupDocument.id,
                                name = groupName,
                                description = groupDescription,
                                advertisementId = advertisementId,
                                qttMembers = qttMembers,
                                isPrivate = isPrivate,
                                photoUri = photoUri
                            )

                            // Adiciona o grupo à lista de grupos de maneira segura
                            synchronized(groupsList) { groupsList.add(group) }

                            // Atualiza o adaptador quando todas as respostas forem recebidas
                            if (remainingRequests.decrementAndGet() == 0) {
                                adapter.updateGroupList(groupsList)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase", "Error fetching group document: $groupRef", exception)
                            if (remainingRequests.decrementAndGet() == 0) {
                                adapter.updateGroupList(groupsList)
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error getting advertisement document: ", exception)
            }


    }
}