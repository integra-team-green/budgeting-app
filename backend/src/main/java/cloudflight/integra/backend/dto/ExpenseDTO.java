package cloudflight.integra.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

// varianta buna
/**
 * Data Transfer Object (DTO) for transferring expense information between the backend and clients
 * via the REST API.
 */
public class ExpenseDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Size(min = 1, max = 50)
    private String category;

    @NotNull
    private LocalDate date;

    @Size(max = 200)
    private String description;

    @NotNull
    private Frequency frequency;

    private LocalDate endDate;

    private LocalDate nextDueDate;

    @NotNull
    private PaymentMethod paymentMethod;

    /**
     * Constructs an {@code ExpenseDTO} with all fields.
     *
     * @param id unique identifier of the expense
     * @param userId ID of the user who owns this expense
     * @param amount monetary value of the expense
     * @param category category of the expense
     * @param date date when the expense occurred
     * @param description optional description of the expense
     * @param frequency frequency of the expense (ONE_TIME, MONTHLY, YEARLY)
     * @param endDate optional end date for recurring expenses
     * @param nextDueDate next due date for recurring expenses
     * @param paymentMethod payment method used (CARD, TRANSFER)
     */
    public ExpenseDTO(
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

    public ExpenseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
