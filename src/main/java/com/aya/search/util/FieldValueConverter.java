package com.aya.search.util;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Field Value Converter.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class FieldValueConverter {

    /**
     * convert field value.
     *
     * @param value value
     * @param fieldType fieldType
     * @return object
     * @throws GenerateSpecificationException GenerateSpecificationException
     */
    public static Object convertFieldValue(final String value, final Class<?> fieldType) {
        try {
            if (fieldType == String.class) {
                return value;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                return Integer.parseInt(value);
            } else if (fieldType == Double.class || fieldType == double.class) {
                return Double.parseDouble(value);
            } else if (fieldType == Float.class || fieldType == float.class) {
                return Float.parseFloat(value);
            } else if (fieldType == Long.class || fieldType == long.class) {
                return Long.parseLong(value);
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (fieldType == LocalDate.class) {
                return LocalDate.parse(value);
            } else if (fieldType == LocalDateTime.class) {
                return LocalDateTime.parse(value);
            } else if (fieldType == OffsetDateTime.class) {
                return OffsetDateTime.parse(value);
            } else {
                return value;
            }
        } catch (Exception e) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE, value, fieldType.toString());
        }
    }
}
