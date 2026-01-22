package com.example.web_market_cosmo_cats.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private String type;
	private String title;
	private Integer status;
	private String detail;
	private String instance;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime timestamp;

	private Map<String, String> errors;

	public static ErrorResponse of(String type, String title, Integer status, String detail, String instance,
			Map<String, String> errors) {
		ErrorResponse response = new ErrorResponse();
		response.type = type;
		response.title = title;
		response.status = status;
		response.detail = detail;
		response.instance = instance;
		response.timestamp = LocalDateTime.now();
		response.errors = (errors != null && !errors.isEmpty()) ? errors : null;

		return response;
	}
}
