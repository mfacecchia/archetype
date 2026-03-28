package ${package}.${artifactId}.common.specification;

import ${package}.${artifactId}.common.exception.BaseException;
import ${package}.${artifactId}.common.exception.errors.Error;
import ${package}.${artifactId}.common.exception.enums.InternalErrorCode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonSpecification<ENTITY> implements Specification<ENTITY> {
    private SearchCriteria criteria;

    public CommonSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUAL_TO -> {
                return builder.equal(getPath(root), criteria.getValue());
            }
            case NOT_EQUAL_TO -> {
                return builder.notEqual(getPath(root), criteria.getValue().toString());
            }
            case GREATER_THAN -> {
                return builder.greaterThan(
                        getPath(root), criteria.getValue().toString());
            }
            case GREATER_THAN_OR_EQUAL_TO -> {
                return builder.greaterThanOrEqualTo(
                        getPath(root), criteria.getValue().toString());
            }
            case LESS_THAN -> {
                return builder.lessThan(
                        getPath(root), criteria.getValue().toString());
            }
            case LESS_THAN_OR_EQUAL_TO -> {
                return builder.lessThanOrEqualTo(
                        getPath(root), criteria.getValue().toString());
            }
            case LIKE -> {
                return builder.like(
                        builder.lower(getPath(root)),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"
                );
            }
            case BETWEEN -> {
                return builder.between(
                        getPath(root),
                        criteria.getValue().toString(),
                        criteria.getValue2().toString());
            }
            case IN -> {
                if (criteria.getValue() instanceof List<?> values) {
                    CriteriaBuilder.In<Object> inClause = builder.in(getPath(root));

                    // Add all values to the IN clause
                    for (Object value : values) {
                        inClause.value(value);
                    }
                    return inClause;
                } else {
                    Error error = new Error(InternalErrorCode.INTERNAL_SEVER_ERROR, "Criteria value for IN operation must be a list");
                    BaseException baseException = new BaseException();
                    baseException.addError(error);
                    throw baseException;
                }
            }
            case NOT_IN -> {
                if (criteria.getValue() instanceof List<?> values) {
                    CriteriaBuilder.In<Object> inClause = builder.in(getPath(root));

                    // Add all values to the IN clause
                    for (Object value : values) {
                        inClause.value(value);
                    }

                    // Negation of the 'IN' clause
                    return builder.not(inClause);
                } else {
                    Error error = new Error(InternalErrorCode.INTERNAL_SEVER_ERROR, "Criteria value for NOT_IN operation must be a list");
                    BaseException baseException = new BaseException();
                    baseException.addError(error);
                    throw baseException;
                }
            }
            case DATE_TO -> {
                switch (criteria.getValue()) {
                    case Instant i -> {
                        return builder.lessThanOrEqualTo(getPath(root), i);
                    }
                    case LocalDateTime ldt -> {
                        return builder.lessThanOrEqualTo(getPath(root), ldt);
                    }
                    case LocalDate ld -> {
                        return builder.lessThanOrEqualTo(getPath(root), ld);
                    }
                    default -> {
                        throw new RuntimeException("Unmanaged type passed as SearchCriteria DATE_TO" + criteria.getValue().getClass().getName());
                    }
                }
            }
            case DATE_FROM -> {
                switch (criteria.getValue()) {
                    case Instant i -> {
                        return builder.greaterThanOrEqualTo(getPath(root), i);
                    }
                    case LocalDateTime ldt -> {
                        return builder.greaterThanOrEqualTo(getPath(root), ldt);
                    }
                    case LocalDate ld -> {
                        return builder.greaterThanOrEqualTo(getPath(root), ld);
                    }
                    default -> {
                        throw new RuntimeException("Unmanaged type passed as SearchCriteria DATE_FROM" + criteria.getValue().getClass().getName());
                    }
                }
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Resolves a dynamic path from the given root based on the
     * dot-separated key provided by the current criteria.
     *
     * This is useful to get the path from nested attributes.
     *
     * @throws IllegalArgumentException if the criteria key is `null`,
     * empty, or refers to a non-existing attribute
     */
    private <T> Path<T> getPath(Root<ENTITY> root) {
        List<String> pathItems = Arrays.asList(criteria.getKey().split("\\."));

        Path<T> path = root.get(pathItems.getFirst());

        for (int i = 1; i < pathItems.size(); i++) {
            path = path.get(pathItems.get(i));
        }

        return path;
    }
}
