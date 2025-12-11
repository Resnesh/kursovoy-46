package kz.edu.java46.library.service;

import java.util.List;
import java.util.Optional;


public interface DataRepository<T, ID> {
    T save(T entity) throws Exception;
    Optional<T> findById(ID id) throws Exception;
    List<T> findAll() throws Exception;
    void deleteById(ID id) throws Exception;
}