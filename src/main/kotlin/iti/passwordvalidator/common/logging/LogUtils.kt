package iti.passwordvalidator.common.logging

import iti.passwordvalidator.common.exception.InternalArchitectureException
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
            log.error(String.format("Error while hashing password, raising InternalArchitectureException"), e)
            throw InternalArchitectureException("Error while hashing password. Please contact the developer(s)")
        }
        return String.format("%040x", BigInteger(1, digest!!.digest()))
    }
}
