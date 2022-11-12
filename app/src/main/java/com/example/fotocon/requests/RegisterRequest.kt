package com.example.fotocon.requests

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class RegisterRequest (
    val email: String,
    val password: String,
    val password_confirmation: String
) {

}