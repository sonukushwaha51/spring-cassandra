package com.spring.boot.labs.spring.cassandra.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TTLRepository<T, ID> {

    T saveWithTtl(T t, int ttl);

}
