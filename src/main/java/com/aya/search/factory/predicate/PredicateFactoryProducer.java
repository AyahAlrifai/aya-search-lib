package com.aya.search.factory.predicate;

import com.aya.search.exception.ErrorCode;
import com.aya.search.exception.GenerateSpecificationException;
import com.aya.search.factory.predicate.impl.BetweenPredicateFactory;
import com.aya.search.factory.predicate.impl.EqualPredicateFactory;
import com.aya.search.factory.predicate.impl.GreaterThanOrEqualPredicateFactory;
import com.aya.search.factory.predicate.impl.GreaterThanPredicateFactory;
import com.aya.search.factory.predicate.impl.InPredicateFactory;
import com.aya.search.factory.predicate.impl.IsEmptyStringPredicateFactory;
import com.aya.search.factory.predicate.impl.IsFalsePredicateFactory;
import com.aya.search.factory.predicate.impl.IsNotEmptyStringPredicateFactory;
import com.aya.search.factory.predicate.impl.IsNotNullPredicateFactory;
import com.aya.search.factory.predicate.impl.IsNullPredicateFactory;
import com.aya.search.factory.predicate.impl.IsTruePredicateFactory;
import com.aya.search.factory.predicate.impl.LessThanOrEqualPredicateFactory;
import com.aya.search.factory.predicate.impl.LessThanPredicateFactory;
import com.aya.search.factory.predicate.impl.LikePredicateFactory;
import com.aya.search.factory.predicate.impl.NotEqualPredicateFactory;
import com.aya.search.factory.predicate.impl.NotInPredicateFactory;
import com.aya.search.factory.predicate.impl.NotLikePredicateFactory;
import com.aya.search.model.Operation;
import java.util.HashMap;
import java.util.Map;

/**
 * Predicate Factory Producer.
 *
 * @author Ayah Alrefai
 * @since 05/25/2024
 */
public class PredicateFactoryProducer {
    private static final Map<Operation, PredicateFactory> FACTORY_MAP = new HashMap<>();

    static {
        FACTORY_MAP.put(Operation.EQUAL, new EqualPredicateFactory());
        FACTORY_MAP.put(Operation.NOT_EQUAL, new NotEqualPredicateFactory());
        FACTORY_MAP.put(Operation.LESS_THAN, new LessThanPredicateFactory());
        FACTORY_MAP.put(Operation.GREATER_THAN, new GreaterThanPredicateFactory());
        FACTORY_MAP.put(Operation.BETWEEN, new BetweenPredicateFactory());
        FACTORY_MAP.put(Operation.GREATER_THAN_EQUAL, new GreaterThanOrEqualPredicateFactory());
        FACTORY_MAP.put(Operation.LESS_THAN_EQUAL, new LessThanOrEqualPredicateFactory());
        FACTORY_MAP.put(Operation.IN, new InPredicateFactory());
        FACTORY_MAP.put(Operation.IS_NULL, new IsNullPredicateFactory());
        FACTORY_MAP.put(Operation.IS_EMPTY_STRING, new IsEmptyStringPredicateFactory());
        FACTORY_MAP.put(Operation.IS_FALSE, new IsFalsePredicateFactory());
        FACTORY_MAP.put(Operation.IS_TRUE, new IsTruePredicateFactory());
        FACTORY_MAP.put(Operation.IS_NOT_EMPTY_STRING, new IsNotEmptyStringPredicateFactory());
        FACTORY_MAP.put(Operation.IS_NOT_NULL, new IsNotNullPredicateFactory());
        FACTORY_MAP.put(Operation.NOT_LIKE, new NotLikePredicateFactory());
        FACTORY_MAP.put(Operation.NOT_IN, new NotInPredicateFactory());
        FACTORY_MAP.put(Operation.LIKE, new LikePredicateFactory());
    }

    /**
     * get predicate factory depends on operation.
     *
     * @param operation operation
     * @return predicate factory
     * @throws GenerateSpecificationException GenerateSpecificationException
     */
    public static PredicateFactory getFactory(final Operation operation) {
        PredicateFactory factory = FACTORY_MAP.get(operation);
        if (factory == null) {
            throw new GenerateSpecificationException(ErrorCode.INVALID_OPERATION, operation.name());
        }
        return factory;
    }
}
