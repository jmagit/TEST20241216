package com.example.domains.core.entities;

public interface EntityValidatable {

	default String getErrorsMessage() {
		return "";
	}

	default boolean isValid() {
		var result = getErrorsMessage();
		return result == null || result.isEmpty() ;
	}

	default boolean isInvalid() {
		return !isValid();
	}

}