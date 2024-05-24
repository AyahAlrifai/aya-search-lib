package com.aya.search.model;

import lombok.Getter;
import java.util.List;

/**
 * Filter Group.
 *
 * @author Ayah Refai
 * @since 05/24/2024
 */
@Getter
public final class FilterGroup implements Filter {

    /**
     * This defines the logical operation that combines the filters within the group.
     * It can be either "AND" or "OR" {@link Condition}
     */
    private final Condition condition;

    /**
     * This is a list of filters that can either be individual FilterCriteria or other FilterGroup objects.
     * This means you can nest groups of filters to create complex filtering logic.
     * <p>can be list of {@link FilterCriteria} or {@link FilterGroup}.</p>
     */
    private final List<com.aya.search.model.Filter> conditions;

    private FilterGroup(final Condition condition,
                        final List<com.aya.search.model.Filter> conditions) {
        this.condition = condition;
        this.conditions = conditions;
    }

    /**
     * Filter.
     */
    public static final class Filter {

        /**
         * create and condition.
         *
         * @param criteria criteria
         * @return FilterGroup
         */
        public static FilterGroup and(final com.aya.search.model.Filter... criteria) {
            return new FilterGroup(Condition.AND, List.of(criteria));
        }

        /**
         * create or condition.
         *
         * @param criteria criteria
         * @return FilterGroup
         */
        public static FilterGroup or(final com.aya.search.model.Filter... criteria) {
            return new FilterGroup(Condition.OR, List.of(criteria));
        }
    }
}