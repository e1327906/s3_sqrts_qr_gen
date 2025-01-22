
package com.qre.cmel.security.component;

import com.google.common.hash.Hashing;
import com.qre.cmel.security.common.Constants;
import com.qre.cmel.security.vo.PemFile;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class Crypto {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Crypto.class);


    @Autowired
    ResourceLoader resourceLoader;

    /**
     *
     * @param messageBytes
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] hashSHA256(String messageBytes) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance(Constants.SHA256);
        return md.digest(messageBytes.getBytes());
    }

    /**
     *
     * @param messageBytes
     * @return
     */
    public String sha256hex(String messageBytes) {
        String sha256hex = Hashing.sha256()
                .hashString(messageBytes, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    /**
     *
     * @param privateKeyFilePath
     * @return
     * @throws Exception
     */
    public PrivateKey getPrivate(String privateKeyFilePath) throws Exception {
        String privateKeyContent = new String(Files.readAllBytes(new File(privateKeyFilePath).toPath()));

        privateKeyContent = privateKeyContent
                .replaceAll("\r\n", "").replaceAll("\n", "")
                .replace(Constants.PEM_RSA_PRIVATE_START, "")
                .replace(Constants.PEM_RSA_PRIVATE_END, "");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        KeyFactory kf = KeyFactory.getInstance(Constants.RSA);
        return kf.generatePrivate(keySpecPKCS8);
    }


    /**
     *
     * @param publicKeyFilePath
     * @return
     * @throws Exception
     */
    public PublicKey getPublicKey(String publicKeyFilePath) throws Exception {
        String publicKeyContent = new String(Files.readAllBytes(new File(publicKeyFilePath).toPath()));
        publicKeyContent = publicKeyContent
                .replaceAll("\r\n", "").replaceAll("\n", "")
                .replace(Constants.PEM_PUBLIC_START, "")
                .replace(Constants.PEM_PUBLIC_END, "");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        KeyFactory kf = KeyFactory.getInstance(Constants.RSA);
        return (RSAPublicKey) kf.generatePublic(keySpecX509);
    }

    /**
     *
     * @param publicKeyPath
     * @param privateKeyPath
     * @throws Exception
     */
    public void generateRSAKeyPair(String publicKeyPath, String privateKeyPath) throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constants.RSA);
        keyPairGenerator.initialize(Constants.KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /*Generate public key PKCS8*/
        generatePublicKeyInPKCS8(keyPair.getPublic(), publicKeyPath);

        /*Generate private key PKCS1*/
        generatePrivateKeyPKCS1ToPEM(keyPair.getPrivate(), privateKeyPath);

    }

    /**
     * Decrypt using RSA public key
     * @param encryptedText
     * @param publicKey
     * @return
     * @throws Exception
     */
    public String decryptMessage(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    /**
     * Encrypt using RSA private key
     * @param plainText
     * @param privateKey
     * @return
     * @throws Exception
     */
    public String encryptMessage(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    /**
     * Decrypt using RSA public key
     * @param encryptedMessage
     * @param publicKey
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] encryptedMessage, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encryptedMessage);
    }

    /**
     * Encrypt using RSA private key
     * @param plainMessage
     * @param privateKey
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String plainMessage, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(hashSHA256(plainMessage));
    }

    /**
     * Decrypt using RSA private key
     * @param encryptedMessage
     * @param privateKey
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] encryptedMessage, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedMessage);
    }

    /**
     * Encrypt using RSA public key
     * @param plainMessage
     * @param publicKey
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String plainMessage, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(hashSHA256(plainMessage));
    }

    /**
     * Decrypt using RSA private key
     * @param encryptedText
     * @param privateKey
     * @return
     * @throws Exception
     */
    public String decryptMessage(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    /**
     * Encrypt using RSA public key
     * @param plainText
     * @param publicKey
     * @return
     * @throws Exception
     */
    public String encryptMessage(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    /**
     *
     * @param privateKey
     * @return
     * @throws IOException
     */
    public byte[] convertPrivateKeyFromPKCS8ToPKCS1(PrivateKey privateKey) throws IOException {

        byte[] privateBytes = privateKey.getEncoded();
        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateBytes);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        return primitive.getEncoded();
    }

    /**
     *
     * @param privateKey
     * @throws IOException
     */
    public void convertPrivateKeyPKCS1ToPEMStr(PrivateKey privateKey) throws IOException {
        PemObject pemObject = new PemObject(Constants.RSA_PRIVATE_KEY, convertPrivateKeyFromPKCS8ToPKCS1(privateKey));
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
    }

    /**
     *
     * @param publicKey
     * @return
     * @throws IOException
     */
    public byte[] convertPublicKeyFromX509ToPKCS1(PublicKey publicKey) throws IOException {
        byte[] pubBytes = publicKey.getEncoded();

        SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(pubBytes);
        ASN1Primitive primitive = spkInfo.parsePublicKey();
        return primitive.getEncoded();
    }

    /**
     *
     * @param privateKey
     * @param privateKeyPath
     * @throws IOException
     */
    public void generatePrivateKeyPKCS1ToPEM(PrivateKey privateKey, String privateKeyPath) throws IOException {
        PemObject pemObject = new PemObject(Constants.RSA_PRIVATE_KEY, convertPrivateKeyFromPKCS8ToPKCS1(privateKey));
        writePemFile(privateKeyPath, pemObject);
    }

    /**
     *
     * @param publicKey
     * @throws IOException
     */
    public void generatePublicKeyInPKCS1ToPEM(PublicKey publicKey, String publicKeyPath) throws IOException {
        PemObject pemObject = new PemObject(Constants.RSA_PUBLIC_KEY, convertPublicKeyFromX509ToPKCS1(publicKey));
        writePemFile(publicKeyPath, pemObject);
    }

    /**
     *
     * @param publicKey
     * @param publicKeyPath
     * @throws IOException
     */
    public void generatePublicKeyInPKCS8(PublicKey publicKey, String publicKeyPath) throws IOException {
        writePemFile(publicKey,Constants.PUBLIC_KEY, publicKeyPath);
    }

    /**
     *
     * @param keyFilePath
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public PrivateKey getPrivateKey(String keyFilePath) throws GeneralSecurityException, IOException {
        //byte[] keyDataBytes = Files.readAllBytes(Paths.get(keyFilePath));
        Resource resource = resourceLoader.getResource(keyFilePath);
        InputStream inputStream = resource.getInputStream();
        byte[] keyDataBytes = inputStream.readAllBytes();
        String keyDataString = new String(keyDataBytes, StandardCharsets.UTF_8);

        if (keyDataString.contains(Constants.PKCS_1_PEM_HEADER)) {
            // OpenSSL / PKCS#1 Base64 PEM encoded file
            keyDataString = keyDataString.replace(Constants.PKCS_1_PEM_HEADER, "");
            keyDataString = keyDataString.replace(Constants.PKCS_1_PEM_FOOTER, "");
            keyDataString = keyDataString.replaceAll("\r\n", "").replaceAll("\n", "");
            return readPkcs1PrivateKey(Base64.getDecoder().decode(keyDataString));
        }

        if (keyDataString.contains(Constants.PKCS_8_PEM_HEADER)) {
            // PKCS#8 Base64 PEM encoded file
            keyDataString = keyDataString.replace(Constants.PKCS_8_PEM_HEADER, "");
            keyDataString = keyDataString.replace(Constants.PKCS_8_PEM_FOOTER, "");
            keyDataString = keyDataString.replaceAll("\r\n", "").replaceAll("\n", "");
            return readPkcs8PrivateKey(Base64.getDecoder().decode(keyDataString));
        }

        // We assume it's a PKCS#8 DER encoded binary file
        return readPkcs8PrivateKey(Files.readAllBytes(Paths.get(keyFilePath)));
    }

    /**
     *
     * @param pkcs8Bytes
     * @return
     * @throws GeneralSecurityException
     */
    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(Constants.RSA);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }

    /**
     *
     * @param pkcs1Bytes
     * @return
     * @throws GeneralSecurityException
     */
    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
        // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can understand
        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + 22;
        byte[] pkcs8Header = new byte[] {
                0x30, (byte) 0x82, (byte) ((totalLength >> 8) & 0xff), (byte) (totalLength & 0xff), // Sequence + total length
                0x2, 0x1, 0x0, // Integer (0)
                0x30, 0xD, 0x6, 0x9, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
                0x4, (byte) 0x82, (byte) ((pkcs1Length >> 8) & 0xff), (byte) (pkcs1Length & 0xff) // Octet string + length
        };
        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
        return readPkcs8PrivateKey(pkcs8bytes);
    }

    /**
     *
     * @param byteArray1
     * @param byteArray2
     * @return
     */
    private static byte[] join(byte[] byteArray1, byte[] byteArray2){
        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
        return bytes;
    }

    /**
     *
     * @param filename
     * @param pemObject
     * @throws IOException
     */
    public void writePemFile(String filename, PemObject pemObject) throws IOException {
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));
        try {
            pemWriter.writeObject(pemObject);
        } finally {
            pemWriter.close();
        }
    }

    /**
     *
     * @param key
     * @param description
     * @param filename
     * @throws IOException
     */
    private void writePemFile(Key key, String description, String filename)
            throws IOException {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(filename);
        logger.info("{} successfully written in file {}.", description, filename);
    }

    /**
     *
     * @param plaintext
     * @param privateKeyFilePath
     * @return
     * @throws Exception
     */
    public byte[] aesEncrypt (byte[] plaintext, String privateKeyFilePath) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance(Constants.AES_CBC);

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, makeKeyWithSalt(privateKeyFilePath), ivSpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    /**
     *
     * @param cipherText
     * @param privateKeyFilePath
     * @return
     * @throws Exception
     */
    public String aesDecrypt (byte[] cipherText, String privateKeyFilePath) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance(Constants.AES_CBC);

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, makeKeyWithSalt(privateKeyFilePath), ivSpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }

    /**
     *
     * @param privateKeyFilePath
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private SecretKey makeKeyWithSalt(String privateKeyFilePath) throws GeneralSecurityException, IOException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(getPurePrivateKey(privateKeyFilePath), Constants.SALT, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), Constants.AES);
        //logger.info("makeKeyWithSalt secret:{}", byteArrayToHex(tmp.getEncoded()));
        return secret;

    }

    /**
     *
     * @param privateKeyFilePath
     * @return
     * @throws IOException
     */
    public char[] getPurePrivateKey(String privateKeyFilePath) throws IOException {
        //String privateKeyPem = new String(Files.readAllBytes(new File(privateKeyFilePath).toPath()));
        Resource resource = resourceLoader.getResource(privateKeyFilePath);
        InputStream inputStream = resource.getInputStream();
        String privateKeyPem = new String(inputStream.readAllBytes());

        privateKeyPem = privateKeyPem.replace(Constants.PEM_RSA_PRIVATE_START, "")
                .replace(Constants.PEM_RSA_PRIVATE_END, "");
        privateKeyPem = privateKeyPem.replaceAll("\r\n", "").replaceAll("\n", "");
        //return org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPem);
        //logger.info("getPurePrivateKey:{}", privateKeyPem);
        return privateKeyPem.toCharArray();
    }
}
