package com.aya.search.factory.predicate.impl;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import com.aya.search.factory.predicate.PredicateFactory;
import com.aya.search.model.Operation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Is Not Null Predicate.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class IsNotNullPredicateFactory implements PredicateFactory {
    @Override
    public Predicate createPredicate(final CriteriaBuilder criteriaBuilder,
                                     final Path<?> fieldPath,
                                     final Object[] values) {
        if (values.length != 0) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE_NUMBERS,
                    Operation.IS_NOT_NULL.name(),
                    Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")));
        }

        return criteriaBuilder.not(criteriaBuilder.isNull(fieldPath));
    }
}