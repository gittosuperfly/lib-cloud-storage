package cai.lib.cloudstorage

import com.qiniu.android.utils.UrlSafeBase64
import org.json.JSONObject
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * 组装七牛云Token
 */
internal object TokenUtils {
    private const val MAC_NAME = "HmacSHA1"
    private const val ENCODING = "UTF-8"

    //Token默认寿命 单位: 秒
    private const val DEFAULT_TOKEN_LIFETIME = 20

    private var deadlineCache = 0L
    private var tokenCache = ""

    fun getToken(config: AccountConfig): String {
        return if (isTokenInvalid()) defaultToken(config) else tokenCache
    }

    private fun defaultToken(config: AccountConfig): String {
        val json = JSONObject()
        val deadline = System.currentTimeMillis() / 1000 + DEFAULT_TOKEN_LIFETIME
        json.put("deadline", deadline)
        json.put("scope", config.scope)
        val encodedPutPolicy = UrlSafeBase64.encodeToString(json.toString().toByteArray())
        val sign = encrypt(encodedPutPolicy, config.secretKey)
        val encodedSign = UrlSafeBase64.encodeToString(sign)
        val token = config.accessKey + ':' + encodedSign + ':' + encodedPutPolicy
        deadlineCache = deadline
        tokenCache = token
        return token
    }

    private fun encrypt(encryptText: String, encryptKey: String): ByteArray? {
        val data = encryptKey.toByteArray(charset(ENCODING))
        val secretKey: SecretKey = SecretKeySpec(data, MAC_NAME)
        val mac = Mac.getInstance(MAC_NAME)
        mac.init(secretKey)
        val text = encryptText.toByteArray(charset(ENCODING))
        return mac.doFinal(text)
    }


    private fun isTokenInvalid(): Boolean {
        if (deadlineCache == 0L) {
            return true
        }
        if (tokenCache.isEmpty()) {
            return true
        }
        //还剩一秒时，保险起见重新申请Token
        return System.currentTimeMillis() / 1000 - deadlineCache >= -1
    }


}