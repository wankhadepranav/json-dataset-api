package com.freightfox.jsondataset.record;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "dataset_records")
public class DatasetRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "dataset_name", nullable = false, length = 100)
	private String datasetName;

	@Lob
	@Column(name = "record_json", nullable = false)
	private String recordJson;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	protected DatasetRecord() {
	}

	public DatasetRecord(String datasetName, String recordJson) {
		this.datasetName = datasetName;
		this.recordJson = recordJson;
	}

	@PrePersist
	void setCreatedAt() {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	public Long getId() {
		return id;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public String getRecordJson() {
		return recordJson;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
