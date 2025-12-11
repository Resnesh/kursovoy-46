// src/kz/edu/java46/library/repository/BookDao.java
package kz.edu.java46.library.repository;

import kz.edu.java46.library.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao {
    private final List<Book> storage = new ArrayList<>();

    public Optional<Book> findById(Long id) {
        return storage.stream().filter(b -> b.getId()!=null && b.getId().equals(id)).findFirst();
    }
    public Book save(Book book) {
        if (book.getId() == null) book.setId((long)(storage.size()+1));
        storage.removeIf(b -> b.getId().equals(book.getId()));
        storage.add(book);
        return book;
    }
    public List<Book> findAll() { return new ArrayList<>(storage); }
}