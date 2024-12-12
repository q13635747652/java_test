package com.hdbsthor012airflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdbsthor012airflow.interfaces.NacosInformationIf;
import com.hdbsthor012airflow.util.BaseUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NacosInformationService implements NacosInformationIf {

    @Autowired
    private Environment environment;

    public String nacosServerURL;
    private String username;
    private String password;
    private String accessToken;
    private long expiryTime;

    @PostConstruct
    private void initialize() {
        readYml();
    }

    private void readYml() {
        nacosServerURL = environment.getProperty("spring.cloud.nacos.config.server-addr");

        // 检查并确保 nacosServerURL 包含协议 (http:// 或 https://)
        if (!nacosServerURL.startsWith("http://") && !nacosServerURL.startsWith("https://")) {
            nacosServerURL = "http://" + nacosServerURL;  // 默认使用 http 协议
        }

        username = environment.getProperty("spring.cloud.nacos.config.username");
        password = environment.getProperty("spring.cloud.nacos.config.password");

        System.out.println(nacosServerURL);
        System.out.println(username);
        System.out.println(password);
    }

    private void fetchToken() throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 设置请求体参数，将 HashMap 改为 MultiValueMap
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("username", username);
            requestBody.add("password", password);

            // 创建请求实体
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

            // 发送POST请求获取令牌
            ResponseEntity<String> response = restTemplate.postForEntity(
                    nacosServerURL + "/nacos/v1/auth/users/login",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());

                // 提取访问令牌
                this.accessToken = jsonResponse.path("accessToken").asText();

                // 提取 tokenTtl，如果不存在，默认为18000秒
                int tokenTtl = jsonResponse.has("tokenTtl")
                        ? jsonResponse.path("tokenTtl").asInt()
                        : 18000;

                // 设置过期时间，提前5分钟
                this.expiryTime = System.currentTimeMillis() + (tokenTtl * 1000L) - (5 * 60 * 1000L);
            } else {
                throw new Exception("Failed to fetch token, status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error fetching Nacos token: " + e.getMessage());
            throw new Exception("Failed to authenticate with Nacos.", e);
        }
    }

    public synchronized String getAccessToken() throws Exception {
        if (accessToken == null || (expiryTime != 0 && System.currentTimeMillis() > expiryTime)) {
            fetchToken();
        }

        if (accessToken == null) {
            throw new Exception("No access token available.");
        }

        return accessToken;
    }

    public ResponseEntity<String> getConfigs(String dataId, String group, String namespace) throws Exception {
        getAccessToken();
        try {
            if (!BaseUtil.Base_HasValue(group)) {
                group = "mk50";
            }
            if (!BaseUtil.Base_HasValue(namespace)) {
                namespace = "public";
            }

            String url = UriComponentsBuilder.fromHttpUrl(nacosServerURL + "/nacos/v2/cs/config")
                    .queryParam("accessToken", accessToken)
                    .queryParam("dataId", dataId)
                    .queryParam("group", group)
                    .queryParam("namespaceId", namespace)
                    .toUriString();
            System.out.println(url);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseData = response.getBody();
                try {
                    responseData = objectMapper.readTree(responseData).path("data").asText();
                    objectMapper.readTree(responseData);
                } catch (Exception e) {
                    System.err.println("JSON解析错误: " + e.getMessage());
                }
            }

            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getMysqlPF(String dataId, String group, String namespace) throws Exception {
        JsonNode dataNode = getDataNode(dataId, group, namespace);
        return extractField(dataNode, "MysqlPF");
    }

    public String getKingHulkServerUrl(String dataId, String group, String namespace) throws Exception {
        JsonNode dataNode = getDataNode(dataId, group, namespace);
        return extractField(dataNode, "KingHulkServer");
    }

    // 提取出公共的获取和解析配置数据的方法
    private JsonNode getDataNode(String dataId, String group, String namespace) throws Exception {
        ResponseEntity<String> responseEntity = getConfigs(dataId, group, namespace);
        String jsonString = responseEntity.getBody();

        // 解析顶层 JSON
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // 提取并解析 data 字段中的嵌套 JSON 字符串
        String dataJsonString = rootNode.path("data").asText();
        return objectMapper.readTree(dataJsonString);
    }

    // 通用字段提取方法
    private String extractField(JsonNode dataNode, String fieldName) {
        String fieldValue = dataNode.path(fieldName).asText(null); // 默认值为 null
        if (fieldValue == null) {
            System.out.println(fieldName + " not found.");
        } else {
            System.out.println(fieldName + ": " + fieldValue);
        }
        return fieldValue;
    }

}