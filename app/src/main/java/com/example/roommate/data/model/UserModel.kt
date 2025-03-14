package com.example.roommate.data.model

import java.io.Serializable
import java.time.LocalDate
import java.util.Date

class UserModel(
    var email: String?,
    var name: String?,
    var bio: String?,
    var sex: String?,         //   var sex: SexEnum?,
    var phone: String?,
    var birthDate: LocalDate?,
    var photo_uri: String?
): Serializable {

    constructor(name: String, bio: String?) : this(null, name, bio, null, null, null, null)
    constructor(name: String, bio: String, phone: String) : this(null, name, bio, null, phone, null, null)
}