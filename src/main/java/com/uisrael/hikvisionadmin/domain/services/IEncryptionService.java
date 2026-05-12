package com.uisrael.hikvisionadmin.domain.services;

public interface IEncryptionService {

  String encrypt(String plainText);

  String decrypt(String encryptedText);
}
