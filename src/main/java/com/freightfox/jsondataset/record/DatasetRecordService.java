package com.freightfox.jsondataset.record;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class DatasetRecordService {

	private static final Pattern DATASET_NAME_PATTERN = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_-]{0,99}");

	private final DatasetRecordRepository datasetRecordRepository;
	private final ObjectMapper objectMapper;

	public DatasetRecordService(DatasetRecordRepository datasetRecordRepository, ObjectMapper objectMapper) {
		this.datasetRecordRepository = datasetRecordRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public DatasetRecord createRecord(String datasetName, String requestBody) {
		if (datasetName == null || !DATASET_NAME_PATTERN.matcher(datasetName).matches()) {
			throw new InvalidDatasetRecordException(
					"Dataset name must be 1 to 100 characters and contain only letters, numbers, underscores, or hyphens; it must start with a letter or number.");
		}
		if (requestBody == null || requestBody.isBlank()) {
			throw new InvalidDatasetRecordException("Request body must contain a JSON object.");
		}

		JsonNode record;
		try {
			record = objectMapper.readTree(requestBody);
		}
		catch (JacksonException exception) {
			throw new InvalidDatasetRecordException("Request body contains malformed JSON.");
		}
		if (record == null || !record.isObject()) {
			throw new InvalidDatasetRecordException("Request body must contain a JSON object.");
		}

		return datasetRecordRepository.saveAndFlush(new DatasetRecord(datasetName, requestBody));
	}
}
