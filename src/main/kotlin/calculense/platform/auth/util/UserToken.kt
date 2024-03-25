package calculense.platform.auth.util

import calculense.platform.exception.CalculenseException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


private const val secretKey = "eea42607362f45c6914a56952add9847018e7434c43a7463b2bff2d381701115018e7437b99171a6b46725cd22b177ea" // Change this to your own secret key
private const val validityInMilliseconds = 86400000 // 24 hour
var secretKeyBytes: ByteArray = secretKey.toByteArray()

// Create a SecretKeySpec object with algorithm HS512
var key: SecretKey = SecretKeySpec(secretKeyBytes, "HmacSHA512")

fun createToken(map:MutableMap<String,Any>): String {
    val now = Date()
    val expiryDate = Date(now.time + validityInMilliseconds)

    return Jwts.builder()
            .setClaims(map)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact()
}

fun parseToken(token:String): Claims {
    try {
        val finalToken = if (token.contains("Bearer")){
            token.replace("Bearer ","")
        }else{
            token
        }
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(finalToken)
                .body
    }catch (e:Exception){
        throw CalculenseException(errorMessage = "Expired/Malformed Token.", errorCode = 401)
    }

}