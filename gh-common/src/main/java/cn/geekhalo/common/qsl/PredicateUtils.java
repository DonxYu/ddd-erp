package cn.geekhalo.common.qsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.util.CollectionUtils;

import java.util.List;

public final class PredicateUtils {
    private PredicateUtils(){}
    public static Predicate buildListAndPredicate(List<PredicateAware> list){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(!CollectionUtils.isEmpty(list)){
            for (PredicateAware predicate : list){
                booleanBuilder.and(predicate.getPredicate());
            }
        }
        return booleanBuilder;
    }

    public static Predicate buildListOrPredicate(List<PredicateAware> list){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(!CollectionUtils.isEmpty(list)){
            for (PredicateAware predicate : list){
                booleanBuilder.or(predicate.getPredicate());
            }
        }
        return booleanBuilder;
    }
}
