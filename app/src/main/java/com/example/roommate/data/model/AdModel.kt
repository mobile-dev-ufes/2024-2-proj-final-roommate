package com.example.roommate.data.model

import com.google.firebase.firestore.QueryDocumentSnapshot
import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale

class Address(
    var cep: String? = "unknown",
    var number: Long? = 0,
    var street: String? = "unknown",
    var nb: String? = "unknown",
    var city: String? = "unknown",
    var state: String? = "unknown"

) : Serializable {
     constructor(data: Map<Any, Any>) : this (
        cep = data["cep"] as? String ?: "unknown",
        number = data["number"] as? Long ? ?: 0L,
        street = data["street"] as? String ?: "unknown",
        nb = data["nb"] as? String ?: "unknown",
        city = data["city"] as? String ?: "unknown",
        state = data["state"] as? String ?: "unknown",
    )

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

class AdModel(
    var id: String? = "unknown",
    var owner: String?,
    var title: String?,
    var rent_value: Double?,
    var cond_value: Double?,
    var max_client: Long?,
    var description: String?,
    var local: Address?,
    var suite_qtt: Long?,
    var bedroom_qtt: Long?,
    var parking_qtt: Long?,
    var area: Double?,
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
    var groups: Array<String>,
    var photos: Array<String> = arrayOf()

) : Serializable {
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
        photos = document.get("photos") as? Array<String> ?: arrayOf()
    )

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

    fun localToString(): String{
        return "${local?.nb}, ${local?.city}"
    }

    fun localCompleteToString(): String{
        return "${local?.street}, n° ${local?.number}, ${local?.nb}, ${local?.city} - ${local?.cep}"
    }

    fun getBenefitsList(): MutableList<String>{
        val trueBenefits = benefits?.filter{ it.value }
            ?.keys
            ?.mapNotNull { customNames[it] }

        return trueBenefits?.toMutableList() ?: mutableListOf()
    }

    fun getValueString(): String{
        val locale = Locale.getDefault()
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val formattedCurrency = currencyFormat.format(this.rent_value)

        return "$formattedCurrency/ mês"
    }
}