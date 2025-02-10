package com.example.plugins
import com.example.models.*

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateJwtToken(user: User, jwtSecret: String): String {
    val algorithm = Algorithm.HMAC256(jwtSecret)

    return JWT.create()
        .withClaim("id", user.id)
        .withClaim("role", user.role)
        .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 24 * 7 * 1000)) // 7 дней
        .sign(algorithm)
}