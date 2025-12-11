package kz.edu.java46.library.dao;

import kz.edu.java46.library.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends AbstractEntity> {

    protected List<T> items = new ArrayList<>();


    public T save(T entity) {
        if (entity.getId() == null) {
            items.add(entity);
            return entity;
        } else {
            findById(entity.getId()).ifPresent(old -> {
                items.remove(old);
                items.add(entity);
            });
            return entity;
        }
    }

    public Optional<T> findById(Long id) {
        return items.stream()
                .filter(item -> item.getId() != null && item.getId().equals(id))
                .findFirst();
    }

    public List<T> findAll() {
        return items;
    }

    public void setItems(List<T> newItems) {
        this.items = newItems;
    }
}