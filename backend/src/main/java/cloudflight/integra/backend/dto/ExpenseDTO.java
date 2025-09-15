package cloudflight.integra.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Data Transfer Object (DTO) for transferring expense information
 * between the backend and clients via the REST API. **/

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


    /**
     * Constructs an {@code ExpenseDto} object with all fields.
     *
     * @param id          the unique identifier of the expense
     * @param userId      the identifier of the user who owns it
     * @param amount      the monetary value
     * @param category    the category of the expense
     * @param date        the date when it occurred
     * @param description an optional description
     */
    public ExpenseDTO(Long id, Long userId, BigDecimal amount, String category, LocalDate date, String description) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

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


}
