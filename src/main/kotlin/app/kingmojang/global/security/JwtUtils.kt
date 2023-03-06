package app.kingmojang.global.security

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Component
class JwtUtils(
    @Value("\${jwt.secret-key}")
    val secret: String,
    @Value("\${jwt.exp-time}")
    val tokenExpTime: Long,
) {
    val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(mutableMapOf(), userDetails)
    }

    fun generateToken(extraClaims: MutableMap<String, Any?>, userDetails: UserDetails): String {
        val currentTime = System.currentTimeMillis()
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(currentTime))
            .setExpiration(Date(currentTime + tokenExpTime.toLong()))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun validation(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            throw CommonException(ErrorCodes.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw CommonException(ErrorCodes.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw CommonException(ErrorCodes.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw CommonException(ErrorCodes.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw CommonException(ErrorCodes.UNHANDLED_EXCEPTION)
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}