package com.uisrael.hikvisionadmin.infrastructure.hikvision;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.services.IHikvisionDeviceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HikvisionIsapiClient implements IHikvisionDeviceService {

  private final ObjectMapper objectMapper;

  // ========== Conexión / Device Info ==========

  @Override
  public boolean testConnection(String ip, int port, String user, String password) {
    try {
      Map<String, Object> info = getDeviceInfo(ip, port, user, password);
      return info != null && !info.isEmpty();
    } catch (Exception e) {
      log.warn("Connection test failed for {}:{} - {}", ip, port, e.getMessage());
      return false;
    }
  }

  @Override
  public Map<String, Object> getDeviceInfo(String ip, int port, String user, String password) {
    String url = buildUrl(ip, port, "/ISAPI/System/deviceInfo");
    String response = executeGet(url, user, password);
    return parseXmlToMap(response);
  }

  // ========== UserInfo ==========

  @Override
  public boolean userExistsOnDevice(String ip, int port, String user, String password, String employeeNo) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/UserInfo/Search?format=json");
    String body = buildUserSearchBody(employeeNo);
    try {
      String response = executePost(url, user, password, body);
      Map<String, Object> result = parseJson(response);
      Map<String, Object> userInfoSearch = getNestedMap(result, "UserInfoSearch");
      if (userInfoSearch == null) return false;
      Object numOfMatches = userInfoSearch.get("numOfMatches");
      return numOfMatches != null && Integer.parseInt(numOfMatches.toString()) > 0;
    } catch (Exception e) {
      log.warn("Error searching user {} on device {}:{} - {}", employeeNo, ip, port, e.getMessage());
      return false;
    }
  }

  @Override
  public void createUserOnDevice(String ip, int port, String user, String password,
      String employeeNo, String name, String userType, String doorRight, boolean localUiRight) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/UserInfo/Record?format=json");
    String body = buildUserRecordBody(employeeNo, name, userType, doorRight, localUiRight);
    String response = executePost(url, user, password, body);
    validateIsapiResponse(response, "createUserOnDevice");
  }

  @Override
  public void modifyUserOnDevice(String ip, int port, String user, String password,
      String employeeNo, String name, String userType, String doorRight, boolean localUiRight) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/UserInfo/Modify?format=json");
    String body = buildUserRecordBody(employeeNo, name, userType, doorRight, localUiRight);
    String response = executePut(url, user, password, body);
    validateIsapiResponse(response, "modifyUserOnDevice");
  }

  @Override
  public void deleteUserFromDevice(String ip, int port, String user, String password, String employeeNo) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/UserInfoDetail/Delete?format=json");
    String body = buildUserDeleteBody(employeeNo);
    String response = executePut(url, user, password, body);
    validateIsapiResponse(response, "deleteUserFromDevice");
  }

  @Override
  public int getUserCount(String ip, int port, String user, String password) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/UserInfo/Count?format=json");
    String response = executeGet(url, user, password);
    Map<String, Object> result = parseJson(response);
    Map<String, Object> userInfoCount = getNestedMap(result, "UserInfoCount");
    if (userInfoCount == null) return 0;
    Object count = userInfoCount.get("userNumber");
    return count != null ? Integer.parseInt(count.toString()) : 0;
  }

  // ========== FaceDataRecord ==========

  @Override
  public boolean faceExistsOnDevice(String ip, int port, String user, String password, String employeeNo) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/FaceDataRecord/Search?format=json");
    String body = buildFaceSearchBody(employeeNo);
    try {
      String response = executePost(url, user, password, body);
      Map<String, Object> result = parseJson(response);
      Map<String, Object> faceDataRecord = getNestedMap(result, "FaceDataRecord");
      if (faceDataRecord == null) return false;
      Object numOfMatches = faceDataRecord.get("numOfMatches");
      return numOfMatches != null && Integer.parseInt(numOfMatches.toString()) > 0;
    } catch (Exception e) {
      log.warn("Error searching face for {} on device {}:{} - {}", employeeNo, ip, port, e.getMessage());
      return false;
    }
  }

  @Override
  public void uploadFaceToDevice(String ip, int port, String user, String password,
      String employeeNo, String faceBase64) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/FaceDataRecord/Record?format=json");
    String body = buildFaceRecordBody(employeeNo, faceBase64);
    String response = executePost(url, user, password, body);
    validateIsapiResponse(response, "uploadFaceToDevice");
  }

  @Override
  public void modifyFaceOnDevice(String ip, int port, String user, String password,
      String employeeNo, String faceBase64) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/FaceDataRecord/Modify?format=json");
    String body = buildFaceRecordBody(employeeNo, faceBase64);
    String response = executePut(url, user, password, body);
    validateIsapiResponse(response, "modifyFaceOnDevice");
  }

  @Override
  public void deleteFaceFromDevice(String ip, int port, String user, String password, String employeeNo) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/FaceDataRecord/Delete?format=json");
    String body = buildFaceDeleteBody(employeeNo);
    String response = executePut(url, user, password, body);
    validateIsapiResponse(response, "deleteFaceFromDevice");
  }

  // ========== Eventos ==========

  @Override
  public Map<String, Object> searchAccessEvents(String ip, int port, String user, String password,
      int searchPosition, int maxResults, String beginTime, String endTime) {
    String url = buildUrl(ip, port, "/ISAPI/AccessControl/AcsEvent?format=json");
    String body = buildEventSearchBody(searchPosition, maxResults, beginTime, endTime);
    String response = executePost(url, user, password, body);
    return parseJson(response);
  }

  @Override
  public byte[] downloadImage(String imageUrl, String user, String password) {
    try (CloseableHttpClient client = createImageClient(user, password)) {
      HttpGet request = new HttpGet(imageUrl);
      request.setHeader("ngrok-skip-browser-warning", "true");
      return client.execute(request, response -> {
        int statusCode = response.getCode();
        if (statusCode < 200 || statusCode >= 300) {
          log.warn("Failed to download image from {} - Status: {}", imageUrl, statusCode);
          return new byte[0];
        }
        HttpEntity entity = response.getEntity();
        return entity != null ? entity.getContent().readAllBytes() : new byte[0];
      });
    } catch (Exception e) {
      log.warn("Error downloading image from {}: {}", imageUrl, e.getMessage());
      return new byte[0];
    }
  }

  // ========== HTTP con DigestAuth ==========

  private CloseableHttpClient createDigestClient(String user, String password) {
    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        new AuthScope(null, -1),
        new UsernamePasswordCredentials(user, password.toCharArray()));

    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(Timeout.ofSeconds(10))
        .setResponseTimeout(Timeout.ofSeconds(30))
        .build();

    return HttpClients.custom()
        .setDefaultCredentialsProvider(credentialsProvider)
        .setDefaultRequestConfig(requestConfig)
        .build();
  }

  // Cliente exclusivo para descarga de imágenes: timeout corto (5s) para no bloquear el servicio
  private CloseableHttpClient createImageClient(String user, String password) {
    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        new AuthScope(null, -1),
        new UsernamePasswordCredentials(user, password.toCharArray()));

    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(Timeout.ofSeconds(5))
        .setResponseTimeout(Timeout.ofSeconds(5))
        .build();

    return HttpClients.custom()
        .setDefaultCredentialsProvider(credentialsProvider)
        .setDefaultRequestConfig(requestConfig)
        .build();
  }

  private String executeGet(String url, String user, String password) {
    HttpGet request = new HttpGet(url);
    return execute(request, user, password);
  }

  private String executePost(String url, String user, String password, String jsonBody) {
    HttpPost request = new HttpPost(url);
    request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
    return execute(request, user, password);
  }

  private String executePut(String url, String user, String password, String jsonBody) {
    HttpPut request = new HttpPut(url);
    request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
    return execute(request, user, password);
  }

  private String execute(ClassicHttpRequest request, String user, String password) {
    try (CloseableHttpClient client = createDigestClient(user, password)) {
      return client.execute(request, response -> {
        int statusCode = response.getCode();
        HttpEntity entity = response.getEntity();
        String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";

        if (statusCode < 200 || statusCode >= 300) {
          log.error("ISAPI request failed: {} {} - Status: {} - Body: {}",
              request.getMethod(), request.getRequestUri(), statusCode, body);
          throw new DomainException("ISAPI request failed with status " + statusCode + ": " + body);
        }

        return body;
      });
    } catch (DomainException e) {
      throw e;
    } catch (IOException e) {
      throw new DomainException("Error connecting to device: " + e.getMessage());
    }
  }

  // ========== URL Builder ==========

  private String buildUrl(String ip, int port, String path) {
    // Soporte para URLs ngrok o cualquier URL completa (https://xxx.ngrok-free.app)
    if (ip.startsWith("http://") || ip.startsWith("https://")) {
      String baseUrl = ip.endsWith("/") ? ip.substring(0, ip.length() - 1) : ip;
      return baseUrl + path;
    }
    return "http://" + ip + ":" + port + path;
  }

  // ========== JSON Body Builders ==========

  private String buildUserSearchBody(String employeeNo) {
    return """
        {
          "UserInfoSearchCond": {
            "searchID": "1",
            "maxResults": 1,
            "searchResultPosition": 0,
            "EmployeeNoList": [
              { "employeeNo": "%s" }
            ]
          }
        }
        """.formatted(employeeNo);
  }

  private String buildUserRecordBody(String employeeNo, String name, String userType,
      String doorRight, boolean localUiRight) {
    String validDoorRight = (doorRight != null && !doorRight.isBlank()) ? doorRight : "1";
    String validUserType = (userType != null && !userType.isBlank()) ? userType : "normal";

    return """
        {
          "UserInfo": {
            "employeeNo": "%s",
            "name": "%s",
            "userType": "%s",
            "Valid": {
              "enable": true,
              "beginTime": "2020-01-01T00:00:00",
              "endTime": "2037-12-31T23:59:59",
              "timeType": "local"
            },
            "doorRight": "%s",
            "RightPlan": [
              {
                "doorNo": 1,
                "planTemplateNo": "1"
              }
            ],
            "localUIRight": %s
          }
        }
        """.formatted(employeeNo, name, validUserType, validDoorRight, localUiRight);
  }

  private String buildUserDeleteBody(String employeeNo) {
    return """
        {
          "UserInfoDetail": {
            "mode": "byEmployeeNo",
            "EmployeeNoList": [
              { "employeeNo": "%s" }
            ]
          }
        }
        """.formatted(employeeNo);
  }

  private String buildFaceSearchBody(String employeeNo) {
    return """
        {
          "FaceDataRecordSearchCond": {
            "searchID": "1",
            "maxResults": 1,
            "searchResultPosition": 0,
            "EmployeeNoList": [
              { "employeeNo": "%s" }
            ]
          }
        }
        """.formatted(employeeNo);
  }

  private String buildFaceRecordBody(String employeeNo, String faceBase64) {
    return """
        {
          "FaceDataRecord": {
            "faceLibType": "blackFD",
            "FPID": "%s",
            "faceURL": "data:image/jpeg;base64,%s"
          }
        }
        """.formatted(employeeNo, faceBase64);
  }

  private String buildFaceDeleteBody(String employeeNo) {
    return """
        {
          "FaceDataRecordDelete": {
            "mode": "byEmployeeNo",
            "EmployeeNoList": [
              { "employeeNo": "%s" }
            ]
          }
        }
        """.formatted(employeeNo);
  }

  private String buildEventSearchBody(int searchPosition, int maxResults, String beginTime, String endTime) {
    return """
        {
          "AcsEventCond": {
            "searchID": "1",
            "searchResultPosition": %d,
            "maxResults": %d,
            "major": 0,
            "minor": 0,
            "startTime": "%s",
            "endTime": "%s"
          }
        }
        """.formatted(searchPosition, maxResults, beginTime, endTime);
  }

  // ========== Parsers ==========

  private Map<String, Object> parseJson(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    } catch (Exception e) {
      log.warn("Error parsing JSON response: {}", e.getMessage());
      return new HashMap<>();
    }
  }

  private Map<String, Object> parseXmlToMap(String xml) {
    // DeviceInfo viene en XML; extraemos campos básicos
    Map<String, Object> result = new HashMap<>();
    result.put("rawResponse", xml);
    result.put("connected", xml != null && xml.contains("DeviceInfo"));

    if (xml != null) {
      result.put("deviceName", extractXmlValue(xml, "deviceName"));
      result.put("model", extractXmlValue(xml, "model"));
      result.put("serialNumber", extractXmlValue(xml, "serialNumber"));
      result.put("firmwareVersion", extractXmlValue(xml, "firmwareVersion"));
      result.put("macAddress", extractXmlValue(xml, "macAddress"));
    }

    return result;
  }

  private String extractXmlValue(String xml, String tag) {
    String openTag = "<" + tag + ">";
    String closeTag = "</" + tag + ">";
    int start = xml.indexOf(openTag);
    int end = xml.indexOf(closeTag);
    if (start >= 0 && end >= 0) {
      return xml.substring(start + openTag.length(), end).trim();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getNestedMap(Map<String, Object> map, String key) {
    Object value = map.get(key);
    if (value instanceof Map) {
      return (Map<String, Object>) value;
    }
    return null;
  }

  private void validateIsapiResponse(String response, String operation) {
    if (response == null || response.isBlank()) return;
    Map<String, Object> result = parseJson(response);
    if (result.containsKey("statusCode") || result.containsKey("subStatusCode")) {
      Object statusCode = result.get("statusCode");
      if (statusCode != null && !"1".equals(statusCode.toString()) && !"ok".equalsIgnoreCase(statusCode.toString())) {
        String subStatus = result.getOrDefault("subStatusCode", "unknown").toString();
        String errorMsg = result.getOrDefault("errorMsg", "").toString();
        log.error("ISAPI {} failed - statusCode: {}, subStatusCode: {}, errorMsg: {}",
            operation, statusCode, subStatus, errorMsg);
        throw new DomainException("ISAPI " + operation + " failed: " + subStatus + " - " + errorMsg);
      }
    }
  }
}
