package com.freightfox.jsondataset.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.freightfox.jsondataset.record.DatasetRecord;
import com.freightfox.jsondataset.record.DatasetRecordRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DatasetRecordControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DatasetRecordRepository datasetRecordRepository;

	@Test
	void createsRecordAndReturnsDatasetAndGeneratedId() throws Exception {
		String datasetName = uniqueDatasetName();

		MvcResult result = mockMvc.perform(post("/api/dataset/{datasetName}/record", datasetName)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"shipmentId":42,"status":"in-transit"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("Record inserted successfully."))
				.andExpect(jsonPath("$.datasetName").value(datasetName))
				.andExpect(jsonPath("$.recordId").isNumber())
				.andReturn();

		long recordId = new tools.jackson.databind.ObjectMapper()
				.readTree(result.getResponse().getContentAsString())
				.get("recordId").asLong();
		DatasetRecord saved = datasetRecordRepository.findById(recordId).orElseThrow();

		assertThat(saved.getDatasetName()).isEqualTo(datasetName);
		assertThat(saved.getRecordJson()).contains("\"shipmentId\":42", "\"status\":\"in-transit\"");
	}

	@Test
	void persistsNestedObjectsAndArrays() throws Exception {
		String datasetName = uniqueDatasetName();
		String json = """
				{"shipment":{"id":42,"items":[{"sku":"A-1"},{"sku":"B-2"}]},"tags":["fragile","express"]}
				""";

		mockMvc.perform(post("/api/dataset/{datasetName}/record", datasetName)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated());

		DatasetRecord saved = datasetRecordRepository.findByDatasetNameOrderByIdAsc(datasetName)
				.stream().findFirst().orElseThrow();
		assertThat(saved.getRecordJson()).isEqualTo(json);
	}

	@Test
	void rejectsInvalidDatasetNameWithConsistentErrorResponse() throws Exception {
		mockMvc.perform(post("/api/dataset/{datasetName}/record", "bad.name")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"value\":1}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	void rejectsMalformedJsonWithConsistentErrorResponse() throws Exception {
		mockMvc.perform(post("/api/dataset/{datasetName}/record", uniqueDatasetName())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"value\":"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Request body contains malformed JSON."));
	}

	@Test
	void rejectsEmptyAndNonObjectRequestBodies() throws Exception {
		String datasetName = uniqueDatasetName();

		mockMvc.perform(post("/api/dataset/{datasetName}/record", datasetName)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Request body must contain a JSON object."));

		mockMvc.perform(post("/api/dataset/{datasetName}/record", datasetName)
						.contentType(MediaType.APPLICATION_JSON)
						.content("[1,2,3]"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Request body must contain a JSON object."));
	}

	private String uniqueDatasetName() {
		return "test_" + UUID.randomUUID().toString().replace("-", "");
	}
}
