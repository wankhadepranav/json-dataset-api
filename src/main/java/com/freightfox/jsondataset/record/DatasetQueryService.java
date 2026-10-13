package com.freightfox.jsondataset.record;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class DatasetQueryService {

    private final DatasetRecordRepository repository;
    private final ObjectMapper objectMapper;

    public DatasetQueryService(
            DatasetRecordRepository repository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> query(
            String datasetName, String groupBy, String sortBy) {

        List<JsonNode> records = new ArrayList<>();

        repository.findByDatasetNameOrderByIdAsc(datasetName)
                .forEach(record -> {
                    try {
                        records.add(objectMapper.readTree(record.getRecordJson()));
                    } catch (Exception e) {
                        throw new IllegalStateException(
                                "Stored JSON is invalid", e);
                    }
                });

        Map<String, Object> response = new LinkedHashMap<>();

        if (groupBy != null && !groupBy.isBlank()) {
            Map<String, List<JsonNode>> groups = new TreeMap<>();

            for (JsonNode record : records) {
                JsonNode value = record.get(groupBy);
                String key = value == null || value.isNull()
                        ? "null"
                        : value.isValueNode()
                            ? value.asText()
                            : value.toString();

                groups.computeIfAbsent(key, k -> new ArrayList<>()).add(record);
            }

            response.put("groupedRecords", groups);
        }

        if (sortBy != null && !sortBy.isBlank()) {
            records.sort((a, b) ->
                    compareValues(a.get(sortBy), b.get(sortBy)));
            response.put("sortedRecords", records);
        }

        return response;
    }

    private int compareValues(JsonNode a, JsonNode b) {
        if (a == null || a.isNull()) {
            return b == null || b.isNull() ? 0 : -1;
        }
        if (b == null || b.isNull()) {
            return 1;
        }

        if (a.isNumber() && b.isNumber()) {
            return a.decimalValue().compareTo(b.decimalValue());
        }

        return a.asText().compareToIgnoreCase(b.asText());
    }
}
