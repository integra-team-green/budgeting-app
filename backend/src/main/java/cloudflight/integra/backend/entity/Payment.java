package cloudflight.integra.backend.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "expense_id", nullable = false) //asigură că fiecare Payment trebuie să aibă un Expense
    private Expense expense;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status; //pending, paid, failed

    private LocalDate paymentDate;

    @Column(nullable = false)
    private BigDecimal amount;

    //private Frequency frequency;
    //private Date nextDueDate;
    //private Boolean isActive;

    // Enum pentru status
    public enum Status {
        PENDING, PAID, FAILED
    }

    public Payment(Expense expense, String name, Status status, LocalDate paymentDate) {
        this.expense = expense;
        this.name = name;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public Payment() {
    }

    public Long getId() {

        return id;
    }
    public void setId(Long id) {

        this.id = id;
    }
    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Expense getExpense() {
        return expense;
    }
    public void setExpense(Expense expense) {
        this.expense = expense;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    //public Frequency getFrequency() {
    //    return frequency;
    //}
    //public void setFrequency(Frequency frequency) {
    //    this.frequency = frequency;
    //}
    //public Date getNextDueDate() {
    //    return nextDueDate;
    //}
    //public void setNextDueDate(Date nextDueDate) {
    //    this.nextDueDate = nextDueDate;
    //}
    //public Boolean getIsActive() {
    //    return isActive;
    //}
    //public void setIsActive(Boolean isActive) {
    //    this.isActive = isActive;
    //}

}
