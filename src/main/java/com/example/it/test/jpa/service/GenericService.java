package com.example.it.test.jpa.service;

import java.util.List;

public interface GenericService<T, U> {

    List<T> findAll();
    T save(T t) throws Exception;
    void delete(U u);
}
