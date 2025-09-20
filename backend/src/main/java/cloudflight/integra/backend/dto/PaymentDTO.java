package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.entity.Frequency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class PaymentDTO {
    private Long id;
    private Long expenseId;
    private String name;
    private BigDecimal amount;
    private String status;
    private LocalDate paymentDate;
    //private Frequency frequency;
    //private Date nextDueDate;
    //private Boolean isActive;
    public PaymentDTO() {

    }
    public PaymentDTO(Long id, Long expenseId, String name, String status, LocalDate paymentDate) {
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
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
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
