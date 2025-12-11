package kz.edu.java46.library.service;

import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class FineService {

    private static final double FINE_PER_DAY = 50.0;


    public Fine calculateFine(Loan loan) {
        if (loan == null) throw new IllegalArgumentException("loan == null");

        LocalDate dueDate = loan.getDateDue();
        if (dueDate == null) {
            Fine zero = new Fine();
            zero.setLoanId(loan.getId());
            zero.setAmount(0.0);
            zero.setPaid(false);
            zero.setDateCalculated(LocalDate.now());
            return zero;
        }

        LocalDate returned = loan.getDateReturned() != null ? loan.getDateReturned() : LocalDate.now();

        long overdueDays = ChronoUnit.DAYS.between(dueDate, returned);
        double totalFine = overdueDays > 0 ? overdueDays * FINE_PER_DAY : 0.0;

        Fine fine = new Fine();
        fine.setLoanId(loan.getId());
        fine.setAmount(totalFine);
        fine.setPaid(false);
        fine.setDateCalculated(LocalDate.now());
        return fine;
    }
}