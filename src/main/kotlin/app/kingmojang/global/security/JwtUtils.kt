package app.kingmojang.global.security

import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import app.kingmojang.global.property.AuthProperty
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Component
class JwtUtils(
    authProperty: AuthProperty,
) {
    companion object {
        const val ISSUER = "Kingmojang"
        const val AUDIENCE = "www.kmj.app"
    }

    private val tokenExpTime = authProperty.expTime
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperty.secretKey))

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userPrincipal: UserPrincipal): String {
        val extraClaim = mutableMapOf<String, Any?>(
            "memberId" to userPrincipal.getId(),
            "nickname" to userPrincipal.getNickname(),
            "memberType" to userPrincipal.getMemberType(),
        )
        return generateToken(extraClaim, userPrincipal)
    }

    fun generateToken(extraClaims: MutableMap<String, Any?>, userPrincipal: UserPrincipal): String {
        val currentTime = System.currentTimeMillis()
        return Jwts.builder()
            .setClaims(extraClaims)
            .setIssuer(ISSUER)
            .setAudience(AUDIENCE)
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date(currentTime))
            .setExpiration(Date(currentTime + tokenExpTime))
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