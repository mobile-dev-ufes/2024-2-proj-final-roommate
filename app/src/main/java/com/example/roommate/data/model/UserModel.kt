package com.example.roommate.data.model

import java.io.Serializable
import java.time.LocalDate

enum class SexEnum {
    MALE, FEMALE, OTHER, UNIDENTIFIED
}

class UserModel (
    var email: String?,
    var name: String?,
    var bio: String?,
    var age: Int?,
    var sex: String?,         //   var sex: SexEnum?,
    var phone: String?,
    var birthDate: String?,  //    var birthDate: LocalDate?,
    var photo_uri: String?
) : Serializable{
    constructor(name: String, bio: String?) : this(null, name, bio, null, null, null, null, null)
}