package com.example.roommate.data.model

import java.io.Serializable
import java.time.LocalDate

class UserModel (
    var email: String? = null,
    var name: String? = null,
    var bio: String? = null,
    var sex: String? = null,
    var phone: String? = null,
    var birthDate: LocalDate? = null,
    var photo_uri: String? = null
) : Serializable{
    constructor(name: String, bio: String?) : this(null, name, bio, null, null, null, null)
    constructor(name: String, bio: String, phone: String) : this(null, name, bio, null, phone, null, null)
}