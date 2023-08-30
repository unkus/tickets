package com.ticketseller.database;

import java.util.Optional;

public interface CrudRepository<T, K> {

    void create(T user);

    Optional<? extends T> findById(K id);

    void update(T object);

    void delete(T object);
}
