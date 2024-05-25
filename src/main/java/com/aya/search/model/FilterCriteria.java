package com.aya.search.model;

import lombok.Getter;

/**
 * Filter Criteria.
 *
 * @author Ayah Refai
 * @since 03/04/2024
 */
@Getter
public final class FilterCriteria implements Filter {

    /**
     * should match the instance variables of your JPA entity.
     * If you need to filter based on a field inside a related object,
     * you can reference it using dot notation
     **/
    private final String fieldName;

    /**
     * The operation to apply (e.g., EQUALS, GREATER_THAN) {@link Operation}
     */
    private final Operation operation;

    /**
     * The value(s) to compare the field against.
     */
    private final Object[] fieldValue;

    private FilterCriteria(final String fieldName,
                           final Operation operation,
                           final Object[] fieldValue) {
        this.fieldName = fieldName;
        this.operation = operation;
        this.fieldValue = fieldValue;
    }

    /**
     * Condition.
     */
    public static final class Condition {

        /**
         * create condition.
         *
         * @param fieldName  fieldName
         * @param operation  operation
         * @param fieldValue fieldValue
         * @return DataManipulationModel
         */
        public static FilterCriteria condition(final String fieldName,
                                               final Operation operation,
                                               final Object... fieldValue) {
            return new FilterCriteria(fieldName, operation, fieldValue);
        }
    }
}
