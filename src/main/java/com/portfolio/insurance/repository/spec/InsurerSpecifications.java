package com.portfolio.insurance.repository.spec;

import com.portfolio.insurance.domain.Insurer;
import org.springframework.data.jpa.domain.Specification;

public class InsurerSpecifications {

    public static Specification<Insurer> nameContains(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
