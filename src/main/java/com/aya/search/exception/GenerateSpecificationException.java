package com.aya.search.exception;

import lombok.Getter;
import java.text.MessageFormat;

/**
 * Generate Specification Exception.
 *
 * @author Ayah Refai
 * @since 05/17/2024
 */
@Getter
public class GenerateSpecificationException extends RuntimeException {

    /**
     * BadRequestException.
     * @param errorCode errorCode
     * @param args      args
     */
    public GenerateSpecificationException(final ErrorCode errorCode, final Object... args) {
        super(args != null ? MessageFormat.format(errorCode.getMessage(), args) : errorCode.getMessage(), null);
    }
}
