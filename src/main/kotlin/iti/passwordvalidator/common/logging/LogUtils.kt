package iti.passwordvalidator.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.security.MessageDigest


object LogUtils {
    private val log = getLogger(this::class.java)

    fun getLogger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)

    fun hash(password: String): String {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance("SHA-1")
            digest.reset()
            digest.update(password.toByteArray(charset("utf8")))
        } catch (e: Exception) {
            log.error(String.format("Error while hashing password, returning [REDACTED]"), e)
            // nao vou estourar erro pois log eh nao-funcional e tem porque parar a aplicacao por isso
            return "[REDACTED]"
        }
        return String.format("%040x", BigInteger(1, digest!!.digest()))
    }
}
