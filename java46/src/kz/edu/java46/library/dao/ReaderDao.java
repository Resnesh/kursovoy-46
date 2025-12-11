// src/kz/edu/java46/library/repository/ReaderDao.java
package kz.edu.java46.library.repository;

import kz.edu.java46.library.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDao {
    private final List<Reader> storage = new ArrayList<>();

    public Optional<Reader> findById(Long id) {
        return storage.stream().filter(r -> r.getId()!=null && r.getId().equals(id)).findFirst();
    }
    public Reader save(Reader reader) {
        if (reader.getId() == null) reader.setId((long)(storage.size()+1));
        storage.removeIf(r -> r.getId().equals(reader.getId()));
        storage.add(reader);
        return reader;
    }
    public List<Reader> findAll() { return new ArrayList<>(storage); }
}