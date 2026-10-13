package com.freightfox.jsondataset.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DatasetRecordRepository extends JpaRepository<DatasetRecord, Long> {

	List<DatasetRecord> findByDatasetNameOrderByIdAsc(String datasetName);
}
