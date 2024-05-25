package com.aya.search.specification;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import com.aya.search.factory.predicate.PredicateFactory;
import com.aya.search.factory.predicate.PredicateFactoryProducer;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * General Specification.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class GeneralSpecification<T> implements Specification<T> {

    private final DataManipulationModel dataManipulationModel;

    public GeneralSpecification(final DataManipulationModel dataManipulationModel) {
        this.dataManipulationModel = dataManipulationModel;
    }

    @Override
    public Predicate toPredicate(@NotNull final Root<T> root,
                                 @NotNull final CriteriaQuery<?> query,
                                 @NotNull final CriteriaBuilder criteriaBuilder) {
        Predicate predicate = buildConditionsPredicate(root, criteriaBuilder, dataManipulationModel.getCriteria());
        if (dataManipulationModel.getSortDataModels() != null && !dataManipulationModel.getSortDataModels().isEmpty()) {
            List<Order> orders = dataManipulationModel.getSortDataModels().stream()
                    .map(sortDataModel -> getOrder(criteriaBuilder, root, sortDataModel))
                    .collect(Collectors.toList());
            query.orderBy(orders);
        }
        return predicate;
    }

    private Order getOrder(final CriteriaBuilder criteriaBuilder,
                           final Root<T> root,
                           final SortDataModel sortDataModel) {
        Expression<?> sortExpression = getSortExpression(root, sortDataModel.getSortField());
        return sortDataModel.getSortOrder() == SortOrder.ASC ? criteriaBuilder.asc(sortExpression) : criteriaBuilder.desc(sortExpression);
    }

    private Expression<?> getSortExpression(final Root<T> root,
                                            final String sortField) {
        try {
            String[] fields = sortField.split("\\.");
            Path<?> expression = root.get(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                expression = expression.get(fields[i]);
            }
            return expression;
        } catch (Exception e) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_SORTING_FIELD, sortField);
        }
    }

    private Predicate buildConditionsPredicate(final Root<T> root,
                                               final CriteriaBuilder criteriaBuilder,
                                               final Object criteria) {
        if (criteria instanceof FilterCriteria) {
            return buildPredicateFromCriteria(root, criteriaBuilder, (FilterCriteria) criteria);
        } else if (criteria instanceof FilterGroup) {
            return buildPredicateFromGroup(root, criteriaBuilder, (FilterGroup) criteria);
        }
        return criteriaBuilder.conjunction();
    }

    private Predicate buildPredicateFromCriteria(final Root<T> root,
                                                 final CriteriaBuilder criteriaBuilder,
                                                 final FilterCriteria filterCriteria) {
        String[] fields = filterCriteria.getFieldName().split("\\.");
        Path<?> fieldPath = null;
        int i = 0;
        try {
            fieldPath = root.get(fields[i]);
            for (i = 1; i < fields.length; i++) {
                fieldPath = fieldPath.get(fields[i]);
            }
        } catch (IllegalArgumentException e) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_FIELD_NAME,
                    fields[i],
                    fieldPath == null ? root.getJavaType().toString() : fieldPath.getJavaType().toString());
        }
        Object[] fieldValues = filterCriteria.getFieldValue();
        Operation operation = filterCriteria.getOperation();
        PredicateFactory predicateFactory = PredicateFactoryProducer.getFactory(operation);
        return predicateFactory.createPredicate(criteriaBuilder, fieldPath, fieldValues);
    }

    private Predicate buildPredicateFromGroup(final Root<T> root,
                                              final CriteriaBuilder criteriaBuilder,
                                              final FilterGroup filterGroup) {
        validateConditions(filterGroup);
        List<Predicate> predicates = filterGroup.getConditions().stream()
                .map(condition -> buildConditionsPredicate(root, criteriaBuilder, condition))
                .toList();
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        return switch (filterGroup.getCondition()) {
            case OR -> criteriaBuilder.or(predicateArray);
            case AND -> criteriaBuilder.and(predicateArray);
            case NOT -> criteriaBuilder.not(predicateArray[0]);
        };
    }

    private void validateConditions(final FilterGroup filterGroup) {

        if(filterGroup.getConditions() == null || filterGroup.getConditions().isEmpty()) {
            throw new GenerateSpecificationException(ErrorCode.EMPTY_CONDITIONS);
        }
        if(filterGroup.getCondition().equals(Condition.NOT) && filterGroup.getConditions().size() > 1) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_CONDITIONS_NUMBER, filterGroup.getCondition(), "equal to 1");
        } else if((filterGroup.getCondition().equals(Condition.OR)
                || filterGroup.getCondition().equals(Condition.AND))
                && filterGroup.getConditions().size() < 2) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_CONDITIONS_NUMBER, filterGroup.getCondition(), "greater than or equal 2");
        }
    }
}
