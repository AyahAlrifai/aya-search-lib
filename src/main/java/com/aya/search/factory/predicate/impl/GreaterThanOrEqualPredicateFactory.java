package com.aya.search.factory.predicate.impl;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import com.aya.search.factory.predicate.PredicateFactory;
import com.aya.search.model.Operation;
import com.aya.search.util.FieldValueConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Greater Than Or Equal Predicate.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class GreaterThanOrEqualPredicateFactory implements PredicateFactory {
    @Override
    public Predicate createPredicate(final CriteriaBuilder criteriaBuilder,
                                     final Path<?> fieldPath,
                                     final Object[] values) {
        if (values.length != 1) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE_NUMBERS,
                    Operation.GREATER_THAN_EQUAL.name(),
                    Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")));
        }

        return criteriaBuilder.greaterThanOrEqualTo((Path<Comparable>) fieldPath,
                (Comparable) FieldValueConverter.convertFieldValue(String.valueOf(values[0]), fieldPath.getJavaType()));
    }
}
