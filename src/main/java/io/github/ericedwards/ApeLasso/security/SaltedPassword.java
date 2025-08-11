package io.github.ericedwards.ApeLasso.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SaltedPassword {

    private static final Logger logger =  LoggerFactory.getLogger(SaltedPassword.class);

    public static boolean check(String saltEncoded, String hashEncoded, String input) {
        try {
            byte[] saltBytes = Hex.decodeHex(saltEncoded.toCharArray());
            String hashedInputEncoded = Hex.encodeHexString(hashPassword(input, saltBytes));
            return hashEncoded.equals(hashedInputEncoded);
        } catch (DecoderException e) {
            // log it
            return false;
        }
    }

    public static String generateSaltEncoded() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[512 / 8];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    public static String encodeHashedPassword(String saltEncoded, String input) {
        try {
            byte[] saltBytes = Hex.decodeHex(saltEncoded.toCharArray());
            return Hex.encodeHexString(hashPassword(input, saltBytes));
        } catch (DecoderException e) {
            // log it
            return null;
        }
    }

    private static byte[] hashPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
        byte[] hash = new byte[0];
        logger.debug("starting hash");
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            logger.error("no such algorithm: ", e);
        } catch (InvalidKeySpecException e) {
            logger.error("invalid key spec: ", e);
        }
        logger.debug("ended hash");
        return hash;
    }

}
