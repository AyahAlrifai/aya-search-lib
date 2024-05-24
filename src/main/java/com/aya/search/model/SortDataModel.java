package com.aya.search.model;

import lombok.Getter;

/**
 * sort data model.
 *
 * @author Ayah Refai
 * @since 03/04/2024
 */
@Getter
public final class SortDataModel {

    /**
     * should match the instance variables of your JPA entity.
     * If you need to sorted based on a field inside a related object,
     * you can reference it using dot notation
     **/
    private final String sortField;

    /**
     * The sort order to apply {@link SortOrder}.
     */
    private final SortOrder sortOrder;

    private SortDataModel(final String sortField,
                          final SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    /**
     * Sort.
     */
    public static final class Sort {

        /**
         * add Sort Model.
         *
         * @param sortField sortField
         * @return DataManipulationModel
         */
        public static SortDataModel asc(final String sortField) {
            return new SortDataModel(sortField, SortOrder.ASC);
        }

        /**
         * add Sort Model.
         *
         * @param sortField sortField
         * @return DataManipulationModel
         */
        public static SortDataModel desc(final String sortField) {
            return new SortDataModel(sortField, SortOrder.DESC);
        }
    }
}
