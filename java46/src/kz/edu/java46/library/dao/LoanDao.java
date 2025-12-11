// src/kz/edu/java46/library/repository/LoanDao.java
package kz.edu.java46.library.repository;

import kz.edu.java46.library.model.Loan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoanDao {
    private final List<Loan> storage = new ArrayList<>();

    public Optional<Loan> findById(Long id) {
        return storage.stream().filter(l -> l.getId()!=null && l.getId().equals(id)).findFirst();
    }
    public Loan save(Loan loan) {
        if (loan.getId() == null) loan.setId((long)(storage.size()+1));
        storage.removeIf(l -> l.getId().equals(loan.getId()));
        storage.add(loan);
        return loan;
    }
    public List<Loan> findAll() { return new ArrayList<>(storage); }

    public List<Loan> findAllOverdueLoans() {
        LocalDate today = LocalDate.now();
        return storage.stream()
                .filter(l -> l.getDateReturned() == null && l.getDateDue() != null && l.getDateDue().isBefore(today))
                .collect(Collectors.toList());
    }
}