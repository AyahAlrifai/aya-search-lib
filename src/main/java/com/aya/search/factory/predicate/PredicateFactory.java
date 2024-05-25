package com.aya.search.factory.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

/**
 * Predicate Factory.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public interface PredicateFactory {

    /**
     * create predicate.
     *
     * @param criteriaBuilder criteriaBuilder
     * @param fieldPath fieldPath
     * @param values values
     * @return Predicate
     */
    Predicate createPredicate(CriteriaBuilder criteriaBuilder, Path<?> fieldPath, Object[] values);
}
