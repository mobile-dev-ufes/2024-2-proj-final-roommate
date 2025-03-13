package com.example.roommate.data.model

import java.io.Serializable
import java.time.LocalDate

enum class SexEnum: Serializable {
    MALE, FEMALE, OTHER, UNIDENTIFIED
}

class UserModel(
    var name: String,
    var bio: String,
    var age: Int?,
    var sex: SexEnum?,
    var birthDate: LocalDate?,
    var photo_uri: String?
) {

    constructor(name: String, bio: String) : this(name, bio, null, null, null, null)
}