package com.example.roommate.data.model

import com.google.firebase.firestore.QueryDocumentSnapshot
import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale

/**
 * Classe que representa um endereço.
 *
 * A classe [Address] contém informações sobre o endereço de um local, incluindo CEP, número da casa,
 * rua, bairro, cidade e estado. Ela é usada tanto em modelos de anúncios como para armazenar a
 * localização de um imóvel.
 *
 * @property cep O CEP do endereço.
 * @property number O número da casa ou apartamento.
 * @property street O nome da rua do endereço.
 * @property nb O nome do bairro.
 * @property city O nome da cidade.
 * @property state O nome do estado.
 */
class Address(
    var cep: String? = "unknown",
    var number: Long? = 0,
    var street: String? = "unknown",
    var nb: String? = "unknown",
    var city: String? = "unknown",
    var state: String? = "unknown"

) : Serializable {

    /**
     * Construtor adicional que cria um [Address] a partir de um mapa de dados.
     *
     * Esse construtor é usado para inicializar um endereço a partir de um mapa, como os dados
     * que vêm de um banco de dados Firebase.
     *
     * @param data O mapa de dados contendo os campos de um endereço.
     */
     constructor(data: Map<Any, Any>) : this (
        cep = data["cep"] as? String ?: "unknown",
        number = data["number"] as? Long ? ?: 0L,
        street = data["street"] as? String ?: "unknown",
        nb = data["nb"] as? String ?: "unknown",
        city = data["city"] as? String ?: "unknown",
        state = data["state"] as? String ?: "unknown",
    )

    /**
     * Converte os dados do endereço para um mapa de chave-valor.
     *
     * @return Um [Map] contendo os dados do endereço.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "cep" to cep,
            "number" to number,
            "street" to street,
            "nb" to nb,
            "city" to city,
            "state" to state
        )
    }
}

/**
 * Classe que representa um anúncio de imóvel.
 *
 * A classe [AdModel] contém as informações de um anúncio de imóvel, como título, preço de aluguel,
 * benefícios, localização e fotos. Ela pode ser usada para gerenciar as propriedades de anúncios,
 * como exibição de dados em uma lista ou em um card de anúncio.
 *
 * @property id O ID do anúncio.
 * @property owner O ID do proprietário do anúncio.
 * @property title O título do anúncio.
 * @property rent_value O valor do aluguel do imóvel.
 * @property cond_value O valor do condomínio do imóvel.
 * @property max_client A quantidade máxima de pessoas para o imóvel.
 * @property description A descrição do imóvel.
 * @property local O endereço do imóvel.
 * @property suite_qtt A quantidade de suítes no imóvel.
 * @property bedroom_qtt A quantidade de quartos no imóvel.
 * @property parking_qtt A quantidade de vagas de estacionamento.
 * @property area A área total do imóvel (em metros quadrados).
 * @property benefits Os benefícios oferecidos no imóvel (ex: internet, garagem, ar condicionado).
 * @property groups Os grupos associados ao anúncio.
 * @property photos As URIs das fotos do imóvel.
 */
class AdModel(
    var id: String? = "unknown",
    var owner: String? = "unknown",
    var title: String? = "unknown",
    var rent_value: Double? = 0.0,
    var cond_value: Double? = 0.0,
    var max_client: Long? = 0,
    var description: String? = "unknown",
    var local: Address? = null,
    var suite_qtt: Long? = 0,
    var bedroom_qtt: Long? = 0,
    var parking_qtt: Long? = 0,
    var area: Double? = 0.0,
    var benefits: Map<String, Boolean>? =  mapOf(
        "ladies" to false,
        "garage" to false,
        "internet" to false,
        "hotWater" to false,
        "ar" to false,
        "pool" to false,
        "pet" to false,
        "security" to false
    ),
    var groups: Array<String> = arrayOf(),
    var photos: MutableList<String> = mutableListOf(),

) : Serializable {

    // Nomes customizados para os benefícios
    val customNames = mapOf(
        "ladies" to "Apenas moças",
        "garage" to "Vaga de garagem",
        "internet" to "Internet de alta velocidade",
        "hotWater" to "Água aquecida",
        "ar" to "Ar condicionado",
        "pool" to "Pscina",
        "pet" to "Pet Friendly",
        "security" to "Segurança"
    )

    /**
     * Construtor adicional que cria um [AdModel] a partir de um documento do Firebase.
     *
     * @param document O documento do Firebase contendo os dados do anúncio.
     */
    constructor(document: QueryDocumentSnapshot) : this(
        id = document.getString("id") ?: "unknown",
        owner = document.getString("owner") ?: "unknown",
        title = document.getString("title") ?: "unknown",
        rent_value = document.getDouble("rent_value") ?: 0.0,
        cond_value = document.getDouble("cond_value") ?: 0.0,
        max_client = document.getLong("max_client") ?: 0,
        description = document.getString("description"),
        suite_qtt = document.getLong("suite_qtt") ?: 0,
        bedroom_qtt = document.getLong("bedroom_qtt") ?: 0,
        parking_qtt = document.getLong("parking_qtt") ?: 0,
        area = document.getDouble("area") ?: 0.0,
        benefits = (document.get("benefits") as? Map<*, *>)?.filterKeys { it is String }
            ?.mapKeys { it.key as String }?.mapValues { it.value as Boolean },
        local = (document.get("local") as? Map<*, *>)?.let { Address(it as Map<Any, Any>) } ?: Address(),
        groups = document.get("groups") as? Array<String> ?: arrayOf(),
        photos = document.get("photos") as? MutableList<String> ?: mutableListOf()
    )

    /**
     * Converte os dados do anúncio para um mapa de chave-valor.
     *
     * @return Um [Map] contendo os dados do anúncio.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "owner" to owner,
            "title" to title,
            "rent_value" to rent_value,
            "cond_value" to cond_value,
            "max_client" to max_client,
            "description" to description,
            "local" to local?.toMap(),
            "suite_qtt" to suite_qtt,
            "bedroom_qtt" to bedroom_qtt,
            "parking_qtt" to parking_qtt,
            "area" to area,
            "benefits" to benefits,
            "groups" to groups.toList(),
            "photos" to photos.toList()
        )
    }

    /**
     * Retorna uma representação simplificada do endereço.
     *
     * @return A string representando a localização (bairro e cidade).
     */
    fun localToString(): String{
        return "${local?.nb}, ${local?.city}"
    }

    /**
     * Retorna a representação completa do endereço.
     *
     * @return A string representando o endereço completo (rua, número, bairro, cidade e CEP).
     */
    fun localCompleteToString(): String{
        return "${local?.street}, n° ${local?.number}, ${local?.nb}, ${local?.city} - ${local?.cep}"
    }

    /**
     * Retorna uma lista de benefícios que estão ativos no imóvel.
     *
     * @return A lista de benefícios disponíveis no imóvel.
     */
    fun getBenefitsList(): MutableList<String>{
        val trueBenefits = benefits?.filter{ it.value }
            ?.keys
            ?.mapNotNull { customNames[it] }

        return trueBenefits?.toMutableList() ?: mutableListOf()
    }

    /**
     * Retorna o valor do aluguel formatado como uma string monetária.
     *
     * @return A string formatada do valor do aluguel.
     */
    fun getValueString(): String{
        val locale = Locale.getDefault()
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val formattedCurrency = currencyFormat.format(this.rent_value)

        return "$formattedCurrency/ mês"
    }
}