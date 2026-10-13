package com.freightfox.jsondataset.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.freightfox.jsondataset.record.InvalidDatasetRecordException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(InvalidDatasetRecordException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidDatasetRecord(InvalidDatasetRecordException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse(exception.getMessage()));
	}
}
