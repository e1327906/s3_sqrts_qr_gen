
package com.qre.cmel.security.component;

import com.qre.cmel.security.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;

@Component
public class DigitalSignature {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(DigitalSignature.class);

    /**
     * crypto
     */
    @Autowired
    private Crypto crypto;


    /**
     *
     * @param plainMessage
     * @param privateKey
     * @return
     * @throws Exception
     */
    public byte[] signingL1(String plainMessage, PrivateKey privateKey) throws Exception {
        return crypto.encrypt(plainMessage,privateKey);
    }

    /**
     *
     * @param encryptedMessage
     * @param publicKey
     * @return
     * @throws Exception
     */
    public byte[] verifyingL1(byte[] encryptedMessage, PublicKey publicKey) throws Exception {
        return crypto.decrypt(encryptedMessage, publicKey);
    }


    /**
     *
     * @param plainMessage
     * @param publicKey
     * @return
     * @throws Exception
     */
    public byte[] signingL1(String plainMessage, PublicKey publicKey) throws Exception {
        return crypto.encrypt(plainMessage,publicKey);
    }

    /**
     *
     * @param encryptedMessage
     * @param privateKey
     * @return
     * @throws Exception
     */
    public byte[] verifyingL1(byte[] encryptedMessage, PrivateKey privateKey) throws Exception {
        return crypto.decrypt(encryptedMessage, privateKey);
    }

    /**
     *
     * @param newMessageHash
     * @param decryptedMessageHash
     * @return
     * @throws Exception
     */
    public boolean isVerifyL1(String newMessageHash, byte[] decryptedMessageHash) throws Exception {
        boolean isCorrect = Arrays.equals(decryptedMessageHash, crypto.hashSHA256(newMessageHash));
        logger.info("Signature isVerifyL1 " + (isCorrect ? "correct" : "incorrect"));
        return isCorrect;
    }

    /**
     *
     * @param plainMessage
     * @param privateKey
     * @return
     * @throws Exception
     */
    public byte[] signingL2(String plainMessage, PrivateKey privateKey) throws Exception {

        Signature signature = Signature.getInstance(Constants.SIGNING_ALGORITHM);
        signature.initSign(privateKey);
        byte[] messageBytes = crypto.hashSHA256(plainMessage);
        signature.update(messageBytes);
        return signature.sign();
    }

    /**
     *
     * @param digitalSignature
     * @param verifyPlainMessage
     * @param publicKey
     * @return
     * @throws Exception
     */
    public boolean isVerifyL2(byte[] digitalSignature, String verifyPlainMessage,
                                     PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(Constants.SIGNING_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(crypto.hashSHA256(verifyPlainMessage));
        boolean isCorrect = signature.verify(digitalSignature);
        logger.info("Signature isVerifyL2 " + (isCorrect ? "correct" : "incorrect"));
        return isCorrect;
    }


    /**
     *
     * @param plainMessage
     * @param keyPath
     * @return
     * @throws Exception
     */
    public byte[] signingL2(String plainMessage, String keyPath) throws Exception {

        Signature signature = Signature.getInstance(Constants.SIGNING_ALGORITHM);
        signature.initSign(crypto.getPrivateKey(keyPath));
        byte[] messageBytes = crypto.hashSHA256(plainMessage);
        signature.update(messageBytes);
        return signature.sign();
    }


    /**
     *
     * @param digitalSignature
     * @param verifyPlainMessage
     * @param keyPath
     * @return
     * @throws Exception
     */
    public boolean isVerifyL2(byte[] digitalSignature, String verifyPlainMessage,
                              String keyPath) throws Exception {
        Signature signature = Signature.getInstance(Constants.SIGNING_ALGORITHM);
        signature.initVerify(crypto.getPublicKey(keyPath));
        signature.update(crypto.hashSHA256(verifyPlainMessage));
        boolean isCorrect = signature.verify(digitalSignature);
        logger.info("Signature isVerifyL2 " + (isCorrect ? "correct" : "incorrect"));
        return isCorrect;
    }
}
