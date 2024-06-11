package com.PetClinic.service.impl;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

    public static <T> Specification<T> attributeLike(String attributeName, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(attributeName)), "%" + value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> attributeEquals(String attributeName, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(attributeName), value);
        };
    }
}