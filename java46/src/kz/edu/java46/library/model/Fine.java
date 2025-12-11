// src/kz/edu/java46/library/model/Fine.java
package kz.edu.java46.library.model;

import java.time.LocalDate;

public class Fine {
    private Long id;
    private Long loanId;
    private double amount;
    private boolean isPaid;
    private LocalDate dateCalculated;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public LocalDate getDateCalculated() { return dateCalculated; }
    public void setDateCalculated(LocalDate dateCalculated) { this.dateCalculated = dateCalculated; }
}