package com.aya.search.specification;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import com.aya.search.model.Condition;
import com.aya.search.model.DataManipulationModel;
import com.aya.search.model.FilterCriteria;
import com.aya.search.model.FilterGroup;
import com.aya.search.model.Operation;
import com.aya.search.model.SortDataModel;
import com.aya.search.model.SortOrder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * General specification.
 */
public class GeneralSpecification<T> implements Specification<T> {

    protected DataManipulationModel dataManipulationModel;

    /**
     * constructor.
     *
     * @param dataManipulationModel dataManipulationModel
     */
    public GeneralSpecification(final DataManipulationModel dataManipulationModel) {
        this.dataManipulationModel = dataManipulationModel;
    }

    private static @NotNull Operation getOperation(final FilterCriteria filterCriteria,
                                                   final Object[] fieldValues) {
        Operation operation = filterCriteria.getOperation();
        if (((operation.equals(Operation.IS_NOT_NULL)
                || operation.equals(Operation.IS_NULL)
                || operation.equals(Operation.IS_TRUE)
                || operation.equals(Operation.IS_FALSE)
                || operation.equals(Operation.IS_EMPTY_STRING)
                || operation.equals(Operation.IS_NOT_EMPTY_STRING))
                && fieldValues.length != 0)
                || (operation.equals(Operation.BETWEEN)
                && fieldValues.length != 2)) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE_NUMBERS,
                    operation.name(),
                    Arrays.stream(fieldValues).map(Object::toString).collect(Collectors.joining(", ")));
        }
        return operation;
    }

    /**
     * to Predicate.
     *
     * @param root            query root
     * @param query           query
     * @param criteriaBuilder criteria builder
     * @return predicate
     */
    @Override
    public Predicate toPredicate(@NotNull final Root<T> root,
                                 @NotNull final CriteriaQuery<?> query,
                                 @NotNull final CriteriaBuilder criteriaBuilder) {

        // search
        Predicate predicate = getConditionsPredicates(root, criteriaBuilder,
                dataManipulationModel.getCriteria());

        // sort
        if (dataManipulationModel.getSortDataModels() != null && !dataManipulationModel.getSortDataModels().isEmpty()) {
            ArrayList<Order> orders = new ArrayList<>();
            for (SortDataModel sortDataModel : dataManipulationModel.getSortDataModels()) {
                Expression<String> sortExpression = getSortExpression(root, sortDataModel.getSortField());
                if (sortDataModel.getSortOrder() == SortOrder.ASC) {
                    orders.add(criteriaBuilder.asc(sortExpression));
                } else {
                    orders.add(criteriaBuilder.desc(sortExpression));
                }
            }
            query.orderBy(orders);
        }
        return predicate;
    }

    /**
     * Get sort expression.
     *
     * @param root      query root.
     * @param sortField sort field
     * @return expression
     * @throws GenerateSpecificationException GenerateSpecificationException
     */
    protected Expression<String> getSortExpression(final Root<T> root, final String sortField) {
        String field = "";
        try {
            String[] fields = sortField.split("\\.");
            Path<String> expression = root.get(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                field = fields[i];
                expression = expression.get(field);
            }
            return expression;
        } catch (Exception e) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_SORTING_FIELD,
                    field);
        }
    }

    /**
     * Gets condition Predicates.
     *
     * @param root            query root
     * @param criteriaBuilder {@link CriteriaBuilder} object
     * @param criteria        query filterCriteria map
     * @return list of predicate
     * @throws GenerateSpecificationException GenerateSpecificationException
     */
    @SuppressWarnings("PMD.UselessParentheses")
    public Predicate getConditionsPredicates(final Root<T> root,
                                             final CriteriaBuilder criteriaBuilder,
                                             final Object criteria) {
        Predicate predicate = null;

        if (criteria instanceof FilterCriteria filterCriteria) {
            String fieldName = filterCriteria.getFieldName();

            String[] fields = fieldName.split("\\.");
            Path<?> fieldPath = null;
            int i = 0;
            try {
                fieldPath = root.get(fields[i]);
                for (i = 1; i < fields.length; i++) {
                    fieldPath = fieldPath.get(fields[i]);
                }
            } catch (IllegalArgumentException e) {
                throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_NAME,
                        fields[i].toString(),
                        fieldPath == null ? root.getJavaType().toString() : fieldPath.getJavaType().toString());
            }

            Object fieldValue = "";
            Object[] fieldValues = filterCriteria.getFieldValue();
            if (fieldValues.length != 0) {
                fieldValue = convertFieldValue(String.valueOf(fieldValues[0]),
                        fieldPath.getJavaType());
            }

            Operation operation = getOperation(filterCriteria, fieldValues);
            Predicate condition;

            switch (operation) {
                case NOT_EQUAL -> condition = criteriaBuilder.notEqual(fieldPath, fieldValue);
                case LESS_THAN ->
                        condition = criteriaBuilder.lessThan((Path<Comparable>) fieldPath, (Comparable) fieldValue);
                case LESS_THAN_EQUAL ->
                        condition = criteriaBuilder.lessThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) fieldValue);
                case GREATER_THAN ->
                        condition = criteriaBuilder.greaterThan((Path<Comparable>) fieldPath, (Comparable) fieldValue);
                case GREATER_THAN_EQUAL ->
                        condition = criteriaBuilder.greaterThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) fieldValue);
                case BETWEEN -> {

                    if (fieldValues.length != 2) {
                        throw new GenerateSpecificationException(ErrorCode.INVALID_OPERATION, "BETWEEN", fieldValues);
                    }

                    Comparable from = (Comparable) convertFieldValue(String.valueOf(fieldValues[0]), fieldPath.getJavaType());
                    Comparable to = (Comparable) convertFieldValue(String.valueOf(fieldValues[1]), fieldPath.getJavaType());
                    condition = criteriaBuilder.between((Path<Comparable>) fieldPath, from, to);
                }
                case IN -> condition = fieldPath.in(fieldValues);
                case NOT_IN -> condition = criteriaBuilder.not(fieldPath.in(fieldValues));
                case LIKE -> condition = criteriaBuilder.like((Path<String>) fieldPath, "%" + fieldValues[0] + "%");
                case NOT_LIKE -> condition =  criteriaBuilder.not(criteriaBuilder.like((Path<String>) fieldPath, "%" + fieldValues[0] + "%"));
                case IS_NULL -> condition = criteriaBuilder.isNull(fieldPath);
                case IS_NOT_NULL -> condition = criteriaBuilder.isNotNull(fieldPath);
                case IS_TRUE -> condition = criteriaBuilder.equal(fieldPath, true);
                case IS_FALSE -> condition = criteriaBuilder.equal(fieldPath, false);
                case IS_EMPTY_STRING -> condition = criteriaBuilder.equal(fieldPath, "");
                case IS_NOT_EMPTY_STRING -> condition = criteriaBuilder.notEqual(fieldPath, "");
                case EQUAL -> condition = criteriaBuilder.equal(fieldPath, fieldValue);
                default -> throw new GenerateSpecificationException(ErrorCode.INVALID_OPERATION,
                        operation);
            }
            return condition;
        } else if (criteria instanceof FilterGroup filterGroup) {

            ArrayList<Predicate> predicates = new ArrayList<Predicate>();
            for (Object filterCriteria : filterGroup.getConditions()) {
                predicates.add(getConditionsPredicates(root, criteriaBuilder, filterCriteria));
            }

            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            if (filterGroup.getCondition().equals(Condition.OR)) {
                predicate = criteriaBuilder.or(predicateArray);
            } else if(filterGroup.getCondition().equals(Condition.AND)) {
                predicate = criteriaBuilder.and(predicateArray);
            } else {
                predicate = criteriaBuilder.not(predicateArray[0]);
            }
            return predicate;
        }

        return null;
    }

    private Object convertFieldValue(final String value, final Class<?> fieldType) {
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
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_VALUE,
                    value,
                    fieldType.toString());
        }
    }
}