package com.freightfox.jsondataset.record;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class DatasetRecordRepositoryTests {

	@Autowired
	private DatasetRecordRepository datasetRecordRepository;

	@Test
	void savesAndLoadsArbitraryJsonForItsDataset() {
		String json = """
				{"shipment":{"id":42,"tags":["fragile","express"]},"active":true}
				""".trim();

		DatasetRecord saved = datasetRecordRepository.saveAndFlush(
				new DatasetRecord("shipments", json));

		datasetRecordRepository.flush();

		DatasetRecord loaded = datasetRecordRepository.findById(saved.getId()).orElseThrow();

		assertThat(loaded.getDatasetName()).isEqualTo("shipments");
		assertThat(loaded.getRecordJson()).isEqualTo(json);
		assertThat(loaded.getCreatedAt()).isNotNull();
		assertThat(datasetRecordRepository.findByDatasetNameOrderByIdAsc("shipments"))
				.extracting(DatasetRecord::getId)
				.containsExactly(saved.getId());
	}
}
