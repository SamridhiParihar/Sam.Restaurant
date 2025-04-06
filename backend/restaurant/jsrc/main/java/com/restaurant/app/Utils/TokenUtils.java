package com.restaurant.app.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String encodeToken(Map<String, AttributeValue> token) {
        if (token == null) return null;
        try {
            Map<String, Map<String, String>> serializableMap = new HashMap<>();
            for (Map.Entry<String, AttributeValue> entry : token.entrySet()) {
                AttributeValue av = entry.getValue();
                Map<String, String> valueMap = new HashMap<>();

                if (av.s() != null) {
                    valueMap.put("S", av.s());
                } else if (av.n() != null) {
                    valueMap.put("N", av.n());
                } else if (av.b() != null) {
                    valueMap.put("B", Base64.getEncoder().encodeToString(av.b().asByteArray()));
                } else if (av.bool() != null) {
                    valueMap.put("BOOL", av.bool().toString());
                }
                // Add other types as needed for your use case

                serializableMap.put(entry.getKey(), valueMap);
            }
            String json = mapper.writeValueAsString(serializableMap);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Token encoding failed", e);
        }
    }

    public static Map<String, AttributeValue> decodeToken(String token) {
        if (token == null) return null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);
            TypeReference<Map<String, Map<String, String>>> typeRef = new TypeReference<>() {};
            Map<String, Map<String, String>> serializedMap = mapper.readValue(json, typeRef);

            Map<String, AttributeValue> result = new HashMap<>();
            for (Map.Entry<String, Map<String, String>> entry : serializedMap.entrySet()) {
                Map<String, String> valueMap = entry.getValue();
                AttributeValue.Builder builder = AttributeValue.builder();

                if (valueMap.containsKey("S")) {
                    builder.s(valueMap.get("S"));
                } else if (valueMap.containsKey("N")) {
                    builder.n(valueMap.get("N"));
                } else if (valueMap.containsKey("B")) {
                    byte[] bytes = Base64.getDecoder().decode(valueMap.get("B"));
                    builder.b(SdkBytes.fromByteArray(bytes));
                } else if (valueMap.containsKey("BOOL")) {
                    builder.bool(Boolean.parseBoolean(valueMap.get("BOOL")));
                }
                // Handle other types as needed

                result.put(entry.getKey(), builder.build());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}