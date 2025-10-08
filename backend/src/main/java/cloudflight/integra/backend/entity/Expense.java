package cloudflight.integra.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "expense", cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private Payment payment;

    public Expense(
            Long id,
            Long userId,
            BigDecimal amount,
            String category,
            LocalDate date,
            String description,
            Frequency frequency,
            LocalDate endDate,
            LocalDate nextDueDate,
            PaymentMethod paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.frequency = frequency;
        this.endDate = endDate;
        this.nextDueDate = nextDueDate;
        this.paymentMethod = paymentMethod;
    }

    public Expense(
            Long id,
            User user,
            BigDecimal amount,
            String category,
            LocalDate date,
            String description,
            Frequency frequency,
            LocalDate endDate,
            LocalDate nextDueDate,
            PaymentMethod paymentMethod) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.frequency = frequency;
        this.endDate = endDate;
        this.nextDueDate = nextDueDate;
        this.paymentMethod = paymentMethod;
    }

    public Expense() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Expense{"
                + "id="
                + id
                + ", amount="
                + amount
                + ", category='"
                + category
                + '\''
                + ", date="
                + date
                + ", description='"
                + description
                + '\''
                + ", frequency="
                + frequency
                + ", endDate="
                + endDate
                + ", nextDueDate="
                + nextDueDate
                + ", paymentMethod="
                + paymentMethod
                + '}';
    }

    public enum Frequency {
        ONE_TIME,
        MONTHLY,
        YEARLY
    }

    public enum PaymentMethod {
        CARD,
        TRANSFER
    }
}
