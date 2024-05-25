package com.aya.search.exception;

import lombok.Getter;

/**
 * Search Lib error codes.
 *
 * @author Ayah Refai
 * @since 05/17/2024
 */
@Getter
public enum ErrorCode {

    INVALID_SORTING_FIELD("AYA-001", "Invalid sorting field {0}"),
    INVALID_OPERATION("AYA-002", "We does not support this operation {0}"),
    INVALID_FIELD_VALUE("AYA-003", "The value {0} is not compatible with type {1}"),
    INVALID_FIELD_VALUE_NUMBERS("AYA-004", "The number of field values [{1}] are not compatible with operation {0}"),
    INVALID_FIELD_NAME("AYA-005","Could not resolve attribute {0} of {1}."),
    EMPTY_CONDITIONS("AYA-006", "The number of conditions can not be zero."),
    INVALID_CONDITIONS_NUMBER("AYA-007", "Invalid conditions number for condition {0}, Should be {1}.");


    /**
     * Error code.
     *
     * @param code code
     * @param message message
     */
    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;
}
