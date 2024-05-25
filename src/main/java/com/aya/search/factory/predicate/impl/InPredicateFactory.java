package com.aya.search.factory.predicate.impl;

import com.aya.search.factory.predicate.PredicateFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

/**
 * In Predicate.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class InPredicateFactory implements PredicateFactory {
    @Override
    public Predicate createPredicate(final CriteriaBuilder criteriaBuilder,
                                     final Path<?> fieldPath,
                                     final Object[] values) {        // TODO
        return fieldPath.in(values);
    }
}