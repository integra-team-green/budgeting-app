package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.entity.Payment;
import java.time.LocalDate;

public class PaymentDTO {
    private Long id;
    private Long expenseId;
    private String name;
    private Payment.StatusEnum status;
    private LocalDate paymentDate;

    public PaymentDTO() {}

    public PaymentDTO(Long id, Long expenseId, String name, Payment.StatusEnum status, LocalDate paymentDate) {
        this.id = id;
        this.expenseId = expenseId;
        this.name = name;
        this.status = status;
        this.paymentDate = paymentDate;
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

    public Payment.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Payment.StatusEnum status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
}
