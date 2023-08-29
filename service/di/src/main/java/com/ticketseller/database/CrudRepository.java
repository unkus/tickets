package com.ticketseller.database;

import java.util.Optional;

public interface CrudRepository<T, K> {

    Optional<T> findById(K id);

    void create(T user);

    T read(K id);

    void update(T object);

    void delete(T object);
}
