package com.freightfox.jsondataset.api;

import com.freightfox.jsondataset.record.DatasetQueryService;
import com.freightfox.jsondataset.record.DatasetRecord;
import com.freightfox.jsondataset.record.DatasetRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
public class DatasetRecordController {

    private final DatasetRecordService datasetRecordService;
    private final DatasetQueryService datasetQueryService;

    public DatasetRecordController(
            DatasetRecordService datasetRecordService,
            DatasetQueryService datasetQueryService) {
        this.datasetRecordService = datasetRecordService;
        this.datasetQueryService = datasetQueryService;
    }

    @PostMapping(
            path = "/{datasetName}/record",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DatasetRecordResponse> createRecord(
            @PathVariable String datasetName,
            @RequestBody(required = false) String requestBody) {

        DatasetRecord record =
                datasetRecordService.createRecord(datasetName, requestBody);

        DatasetRecordResponse response = new DatasetRecordResponse(
                "Record inserted successfully.",
                record.getDatasetName(),
                record.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = "/{datasetName}/query",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> queryRecords(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy) {

        Map<String, Object> result =
                datasetQueryService.query(datasetName, groupBy, sortBy);

        return ResponseEntity.ok(result);
    }
}
