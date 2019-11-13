package cn.geekhalo.common.qsl;

import com.querydsl.core.types.Predicate;

public interface PredicateAware {
    Predicate getPredicate();
}
