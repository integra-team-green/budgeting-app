package cloudflight.integra.backend.entity;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Credit entity - plain JPA entity (no foreign keys).
 */
//@Entity
//@Table(name = "credits")
public class Credit {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Required fields
    //@Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    //@Column(nullable = false)
    private String lender;

    //@Column(nullable = false)
    private LocalDate startDate;

    //@Column(nullable = false)
    private LocalDate dueDate;

    //@Column(nullable = false)
    private Double interestRate;

    //@Column(length = 2000)
    private String description;

    // Empty constructor required by JPA
    public Credit() {
    }

    // Full constructor
    public Credit(Long id, BigDecimal amount, String lender, LocalDate startDate, LocalDate dueDate,
                  Double interestRate, String description) {
        this.id = id;
        this.amount = amount;
        this.lender = lender;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.interestRate = interestRate;
        this.description = description;
    }

    // Builder-style setters / fluent API
    public Credit withId(Long id) {
        this.id = id;
        return this;
    }

    public Credit withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Credit withLender(String lender) {
        this.lender = lender;
        return this;
    }

    public Credit withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Credit withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Credit withInterestRate(Double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public Credit withDescription(String description) {
        this.description = description;
        return this;
    }

    // Standard getters/setters (non-fluent)
    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getLender() {
        return lender;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(id, credit.id) && Objects.equals(amount, credit.amount) && Objects.equals(lender,
            credit.lender) && Objects.equals(startDate, credit.startDate) && Objects.equals(dueDate,
            credit.dueDate) && Objects.equals(interestRate, credit.interestRate) && Objects.equals(description,
            credit.description);
    }
}

