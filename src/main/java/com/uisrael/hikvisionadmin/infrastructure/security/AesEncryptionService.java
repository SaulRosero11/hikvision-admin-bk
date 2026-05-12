package com.uisrael.hikvisionadmin.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.services.IEncryptionService;

@Component
public class AesEncryptionService implements IEncryptionService {

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final byte[] IV = new byte[16]; // IV fijo para simplicidad; en producción usar IV aleatorio
  private static final String SALT = "HikvisionAdminSalt";
  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 256;

  private final SecretKey secretKey;

  public AesEncryptionService(@Value("${jwt.secret}") String secret) {
    try {
      KeySpec spec = new PBEKeySpec(secret.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8),
          ITERATION_COUNT, KEY_LENGTH);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
      byte[] keyBytes = factory.generateSecret(spec).getEncoded();
      this.secretKey = new SecretKeySpec(keyBytes, "AES");
    } catch (Exception e) {
      throw new DomainException("Error initializing encryption service: " + e.getMessage());
    }
  }

  @Override
  public String encrypt(String plainText) {
    if (plainText == null || plainText.isBlank()) {
      return plainText;
    }
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));
      byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new DomainException("Error encrypting data: " + e.getMessage());
    }
  }

  @Override
  public String decrypt(String encryptedText) {
    if (encryptedText == null || encryptedText.isBlank()) {
      return encryptedText;
    }
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
      byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new DomainException("Error decrypting data: " + e.getMessage());
    }
  }
}
