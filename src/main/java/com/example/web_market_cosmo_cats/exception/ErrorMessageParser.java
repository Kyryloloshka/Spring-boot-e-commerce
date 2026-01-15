package com.example.web_market_cosmo_cats.exception;

import java.util.Map;

public class ErrorMessageParser {

	public static ParseResult parseDeserializationError(String message) {
		if (message == null) {
			return new ParseResult("Invalid request body format.", Map.of());
		}

		if (message.contains("Cannot deserialize value")) {
			String detail = extractFieldErrorFromMessage(message);
			Map<String, String> fieldErrors = extractFieldErrors(message);

			return new ParseResult(detail, fieldErrors);
		}

		if (message.contains("Unexpected character")) {
			return new ParseResult("Invalid JSON format. Please check your JSON syntax.", Map.of());
		}

		if (message.contains("Required request body is missing")) {
			return new ParseResult("Request body is required but was not provided.", Map.of());
		}

		if (message.contains("JSON parse error")) {
			String usefulPart = message.contains(": ") ? message.substring(message.indexOf(": ") + 2) : message;

			if (usefulPart.contains("Cannot deserialize value")) {
				String detail = extractFieldErrorFromMessage(usefulPart);
				Map<String, String> fieldErrors = extractFieldErrors(usefulPart);
				return new ParseResult(detail, fieldErrors);
			}

			return new ParseResult("Invalid JSON format: " + sanitizeMessage(usefulPart), Map.of());
		}

		return new ParseResult("Invalid request body: " + sanitizeMessage(message), Map.of());
	}

	private static String extractFieldErrorFromMessage(String message) {
		String fieldName = extractFieldName(message);
		String typeName = extractTypeName(message);

		if (fieldName != null) {
			return String.format("Invalid value for field '%s': expected %s, but received invalid format.", fieldName,
					typeName);
		}

		return "Invalid request body format. Please check field types and values.";
	}

	private static Map<String, String> extractFieldErrors(String message) {
		String fieldName = extractFieldName(message);
		String typeName = extractTypeName(message);

		if (fieldName != null && typeName != null) {
			return Map.of(fieldName, String.format("Expected %s, but received invalid format", typeName));
		}

		return Map.of();
	}

	private static String extractFieldName(String message) {
		int fromIndex = message.indexOf("from String \"");

		if (fromIndex > 0) {
			int start = fromIndex + "from String \"".length();
			int end = message.indexOf("\"", start);

			if (end > start) {
				return message.substring(start, end);
			}
		}

		return null;
	}

	private static String extractTypeName(String message) {
		int typeIndex = message.indexOf("type `");

		if (typeIndex > 0) {
			int start = typeIndex + "type `".length();
			int end = message.indexOf("`", start);

			if (end > start) {
				String fullType = message.substring(start, end);

				if (fullType.contains(".")) {
					return fullType.substring(fullType.lastIndexOf(".") + 1);
				}

				return fullType;
			}
		}

		return "unknown type";
	}

	private static String sanitizeMessage(String message) {
		if (message.length() > 200) {
			return message.substring(0, 197) + "...";
		}

		return message;
	}
}
