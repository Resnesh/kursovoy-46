// src/kz/edu/java46/library/repository/FineDao.java
package kz.edu.java46.library.repository;

import kz.edu.java46.library.model.Fine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineDao {
    private final List<Fine> storage = new ArrayList<>();

    public Optional<Fine> findById(Long id) {
        return storage.stream().filter(f -> f.getId()!=null && f.getId().equals(id)).findFirst();
    }
    public Fine save(Fine fine) {
        if (fine.getId() == null) fine.setId((long)(storage.size()+1));
        storage.removeIf(f -> f.getId().equals(fine.getId()));
        storage.add(fine);
        return fine;
    }
    public List<Fine> findAll() { return new ArrayList<>(storage); }
}