package com.tumble.kronoxtoapp.services

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Singleton

@Singleton
class SecureStorageService(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tumble_secure_prefs", Context.MODE_PRIVATE)
    private val keyAlias = "TumbleSecureStorageKey"

    init {
        generateKeyIfNeeded()
    }

    private fun generateKeyIfNeeded() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    private fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val combined = iv + encryptedData
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private fun decrypt(encryptedData: String): String? {
        return try {
            val combined = Base64.decode(encryptedData, Base64.DEFAULT)

            val iv = combined.sliceArray(0..11)
            val encrypted = combined.sliceArray(12 until combined.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

            val decryptedData = cipher.doFinal(encrypted)
            String(decryptedData, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun save(service: String, account: String, data: String) {
        val key = "$service-$account"
        val encryptedData = encrypt(data)
        sharedPreferences.edit().putString(key, encryptedData).apply()
    }

    fun read(service: String, account: String): String? {
        val key = "$service-$account"
        val encryptedData = sharedPreferences.getString(key, null) ?: return null
        return decrypt(encryptedData)
    }

    fun delete(service: String, account: String) {
        val key = "$service-$account"
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun deleteKey() {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            keyStore.deleteEntry(keyAlias)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}