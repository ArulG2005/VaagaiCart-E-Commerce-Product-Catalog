package com.vaagaicart.backend.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiFeatures<T> {
    
    private Specification<T> specification;
    private Pageable pageable;
    private final Map<String, String> queryParams;
    
    public ApiFeatures(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        this.specification = Specification.where(null);
    }
    
    public ApiFeatures<T> search() {
        if (queryParams.containsKey("keyword")) {
            String keyword = queryParams.get("keyword");
            specification = specification.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }
        return this;
    }
    
    public ApiFeatures<T> filter() {
        queryParams.entrySet().stream()
            .filter(entry -> !List.of("keyword", "limit", "page").contains(entry.getKey()))
            .forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                specification = specification.and((root, query, cb) -> {
                    if (value.startsWith(">=")) {
                        return cb.greaterThanOrEqualTo(root.get(key), value.substring(2));
                    } else if (value.startsWith(">")) {
                        return cb.greaterThan(root.get(key), value.substring(1));
                    } else if (value.startsWith("<=")) {
                        return cb.lessThanOrEqualTo(root.get(key), value.substring(2));
                    } else if (value.startsWith("<")) {
                        return cb.lessThan(root.get(key), value.substring(1));
                    } else {
                        return cb.equal(root.get(key), value);
                    }
                });
            });
        return this;
    }
    
    public ApiFeatures<T> paginate() {
        int page = Integer.parseInt(queryParams.getOrDefault("page", "1")) - 1;
        int limit = Integer.parseInt(queryParams.getOrDefault("limit", "10"));
        this.pageable = PageRequest.of(page, limit);
        return this;
    }
    
    public Specification<T> getSpecification() {
        return specification;
    }
    
    public Pageable getPageable() {
        return pageable != null ? pageable : Pageable.unpaged();
    }
}