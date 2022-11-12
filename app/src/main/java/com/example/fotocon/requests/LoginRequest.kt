package com.example.fotocon.requests

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class LoginRequest (
    val email: String,
    val password: String,
) {

}