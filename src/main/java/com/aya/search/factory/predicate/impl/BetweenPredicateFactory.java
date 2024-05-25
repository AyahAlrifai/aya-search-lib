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
 * Between predicate.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class BetweenPredicateFactory implements PredicateFactory {
    @Override
    public Predicate createPredicate(final CriteriaBuilder criteriaBuilder,
                                     final Path<?> fieldPath,
                                     final Object[] values) {
        if (values.length != 2) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE_NUMBERS,
                    Operation.BETWEEN.name(),
                    Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")));
        }

        Comparable from = (Comparable) FieldValueConverter.convertFieldValue(String.valueOf(values[0]), fieldPath.getJavaType());
        Comparable to = (Comparable) FieldValueConverter.convertFieldValue(String.valueOf(values[1]), fieldPath.getJavaType());
        return criteriaBuilder.between((Path<Comparable>) fieldPath, from, to);
    }
}