package com.weChatCard.utils;

import com.weChatCard.bo.SearchPara;
import com.weChatCard.bo.SearchParas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MySpecification<T> implements Specification<T> {
    private static Logger log = LoggerFactory.getLogger(MySpecification.class);
    private SearchParas searchParas;

    public MySpecification(SearchParas searchParas) {
        this.searchParas = searchParas;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        //搜索查询条件
        Predicate searchPredicate = cb.conjunction();

        if (searchParas == null) {
            return cb.conjunction();
        }
        if (searchParas.getLogic() != null && "OR".equalsIgnoreCase(searchParas.getLogic())) {
            searchPredicate = cb.disjunction();
        }
        List<Expression<Boolean>> expressions = searchPredicate.getExpressions();
        if (searchParas != null && searchParas.getConditions() != null) {
            for (SearchPara searchPara : searchParas.getConditions()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                switch (searchPara.getOp()) {
                    case "like": {
                        expressions.add(cb.like(root.get(searchPara.getName()), "%" + searchPara.getValue() + "%"));
                        break;
                    }
                    case "eq": {
                        expressions.add(cb.equal(root.get(searchPara.getName()), searchPara.getValue()));
                        break;
                    }
                    case "noeq": {
                        expressions.add(cb.notEqual(root.get(searchPara.getName()), searchPara.getValue()));
                        break;
                    }
                    case "lt": {
                        if ("date".equalsIgnoreCase(searchPara.getType())) {
                            try {
                                Date date = sdf.parse(searchPara.getValue().toString());
                                expressions.add(cb.lessThanOrEqualTo(root.<Date>get(searchPara.getName()), date));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            expressions.add(cb.lt(root.get(searchPara.getName()), (Number) searchPara.getValue()));
                        }
                        break;
                    }
                    case "le": {
                        if ("date".equalsIgnoreCase(searchPara.getType())) {
                            try {
                                Date date = sdf.parse(searchPara.getValue().toString());
                                expressions.add(cb.lessThan(root.<Date>get(searchPara.getName()), date));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            expressions.add(cb.le(root.get(searchPara.getName()), (Number) searchPara.getValue()));
                        }
                        break;
                    }
                    case "gt": {
                        if ("date".equalsIgnoreCase(searchPara.getType())) {
                            try {
                                Date date = sdf.parse(searchPara.getValue().toString());
                                expressions.add(cb.greaterThanOrEqualTo(root.<Date>get(searchPara.getName()), date));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            expressions.add(cb.gt(root.get(searchPara.getName()), (Number) searchPara.getValue()));
                        }
                        break;
                    }
                    case "ge": {
                        if ("date".equalsIgnoreCase(searchPara.getType())) {
                            try {
                                Date date = sdf.parse(searchPara.getValue().toString());
                                expressions.add(cb.greaterThan(root.<Date>get(searchPara.getName()), date));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            expressions.add(cb.ge(root.get(searchPara.getName()), (Number) searchPara.getValue()));
                        }
                        break;
                    }
                    case "isnull": {
                        expressions.add(cb.isNull(root.get(searchPara.getName())));
                        break;
                    }
                    case "isnotnull": {
                        expressions.add(cb.isNotNull(root.get(searchPara.getName())));
                        break;
                    }
                    case "in": {
                        CriteriaBuilder.In in = cb.in(root.get(searchPara.getName()));
                        List<Integer> vals = (List<Integer>) searchPara.getValue();
                        for (Integer val : vals) {
                            in.value(val);
                        }
                        expressions.add(in);
                        break;
                    }
                    default: {
                        //op 错误
                        expressions.add(cb.equal(root.get("1"), "-1"));
                        break;
                    }
                }
            }
        }
        return searchPredicate;
    }
}