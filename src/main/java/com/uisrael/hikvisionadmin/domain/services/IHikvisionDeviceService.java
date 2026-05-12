package com.uisrael.hikvisionadmin.domain.services;

import java.util.Map;

public interface IHikvisionDeviceService {

  // ========== Conexión / Device Info ==========

  /**
   * Prueba la conexión al dispositivo llamando GET /ISAPI/System/deviceInfo.
   * Retorna true si la conexión y autenticación son exitosas.
   */
  boolean testConnection(String ip, int port, String user, String password);

  /**
   * Obtiene la información del dispositivo (modelo, firmware, serial, etc.).
   * GET /ISAPI/System/deviceInfo
   */
  Map<String, Object> getDeviceInfo(String ip, int port, String user, String password);

  // ========== UserInfo (Personas en el dispositivo) ==========

  /**
   * Busca si un usuario (employeeNo) ya existe en el dispositivo.
   * POST /ISAPI/AccessControl/UserInfo/Search?format=json
   */
  boolean userExistsOnDevice(String ip, int port, String user, String password, String employeeNo);

  /**
   * Crea una persona en el dispositivo.
   * POST /ISAPI/AccessControl/UserInfo/Record?format=json
   */
  void createUserOnDevice(String ip, int port, String user, String password,
      String employeeNo, String name, String userType, String doorRight, boolean localUiRight);

  /**
   * Modifica datos de una persona en el dispositivo.
   * PUT /ISAPI/AccessControl/UserInfo/Modify?format=json
   */
  void modifyUserOnDevice(String ip, int port, String user, String password,
      String employeeNo, String name, String userType, String doorRight, boolean localUiRight);

  /**
   * Elimina una persona del dispositivo.
   * PUT /ISAPI/AccessControl/UserInfoDetail/Delete?format=json
   */
  void deleteUserFromDevice(String ip, int port, String user, String password, String employeeNo);

  /**
   * Cuenta cuántas personas hay registradas en el dispositivo.
   * GET /ISAPI/AccessControl/UserInfo/Count?format=json
   */
  int getUserCount(String ip, int port, String user, String password);

  // ========== FaceDataRecord (Imágenes faciales) ==========

  /**
   * Verifica si ya existe una foto facial para el empleado.
   * POST /ISAPI/AccessControl/FaceDataRecord/Search
   */
  boolean faceExistsOnDevice(String ip, int port, String user, String password, String employeeNo);

  /**
   * Envía la foto facial en Base64 al dispositivo.
   * POST /ISAPI/AccessControl/FaceDataRecord/Record
   */
  void uploadFaceToDevice(String ip, int port, String user, String password,
      String employeeNo, String faceBase64);

  /**
   * Actualiza la foto facial si cambió.
   * PUT /ISAPI/AccessControl/FaceDataRecord/Modify
   */
  void modifyFaceOnDevice(String ip, int port, String user, String password,
      String employeeNo, String faceBase64);

  /**
   * Elimina la foto facial del dispositivo.
   * PUT /ISAPI/AccessControl/FaceDataRecord/Delete
   */
  void deleteFaceFromDevice(String ip, int port, String user, String password, String employeeNo);

  // ========== Eventos de Acceso ==========

  /**
   * Busca historial de eventos de acceso.
   * POST /ISAPI/AccessControl/AcsEvent?format=json
   */
  Map<String, Object> searchAccessEvents(String ip, int port, String user, String password,
      int searchPosition, int maxResults, String beginTime, String endTime);

  /**
   * Descarga una imagen desde una URL del dispositivo usando DigestAuth.
   * Retorna byte[] vacío si falla en lugar de lanzar excepción.
   */
  byte[] downloadImage(String imageUrl, String user, String password);
}
