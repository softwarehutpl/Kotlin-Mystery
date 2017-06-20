package com.softwarehut.kotlinmystery

import android.content.Context
import org.apache.commons.codec.binary.Base64
import java.io.*
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher


class MysteryService(val context: Context) {
    fun getSecretMessage(): String {
        val _keyPair = readKeyFromAssets("key_pair.serialized") as KeyPair
        val data = readBytesFromAssets("message.bytes")
        return RSADecrypt(data, _keyPair.private)
    }

    private val keyPairGenerator: KeyPairGenerator

    private val keyPair: KeyPair

    private val publicKey: PublicKey

    private var privateKey: PrivateKey

    init {
        //generating new key pair, always different
        //but message was decrypted with already existing keypair, we can't use it
        keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048 * 2)

        keyPair = keyPairGenerator.genKeyPair()
        publicKey = keyPair.public
        privateKey = keyPair.private

    }

    fun RSAEncrypt(plain: String, key: PublicKey = publicKey): ByteArray {

        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val encryptedBytes = cipher.doFinal(plain.toByteArray())
        System.out.println("Encrypted?????" + Base64.encodeBase64(encryptedBytes))
        return encryptedBytes
    }

    fun RSADecrypt(encryptedBytes: ByteArray, key: PrivateKey = privateKey): String {

        val cipher1 = Cipher.getInstance("RSA")
        cipher1.init(Cipher.DECRYPT_MODE, key)
        val decryptedBytes = cipher1.doFinal(encryptedBytes)
        val decrypted = String(decryptedBytes)
        System.out.println("Decrypted?????" + decrypted)
        return decrypted
    }

    private fun readObjectFromFile(filePath: String): Any {
        val fis = FileInputStream(filePath)
        val ois = ObjectInputStream(fis)
        return ois.readObject()
    }

    fun saveObjectToFile(ob: Serializable, filePath: String) {
        val fout = FileOutputStream(filePath)
        val oos = ObjectOutputStream(fout)
        oos.writeObject(ob)
    }

    fun readKeyFromAssets(fileName: String): Any {
        val oos = ObjectInputStream(context.assets.open(fileName))
        return oos.readObject()
    }

    fun readBytesFromAssets(fileName: String): ByteArray {
        val stream = context.assets.open(fileName)
        val fileBytes = ByteArray(stream.available())
        stream.read(fileBytes)
        stream.close()
        return fileBytes
    }

    fun saveByteArrayToFile(name: String, data: ByteArray, filePath: String) {
        val fos = FileOutputStream(filePath)
        fos.write(data)
        fos.close()
    }

}
