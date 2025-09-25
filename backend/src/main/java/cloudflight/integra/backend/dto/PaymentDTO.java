package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDTO {
  private Long id;
  private ExpenseDTO expense;
  private String name;
  private BigDecimal amount;
  private Payment.StatusEnum status;
  private LocalDate paymentDate;

  public PaymentDTO() {}

  public PaymentDTO(
      Long id,
      ExpenseDTO expense,
      String name,
      BigDecimal amount,
      Payment.StatusEnum status,
      LocalDate paymentDate) {
    this.id = id;
    this.expense = expense;
    this.name = name;
    this.amount = amount;
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

  public ExpenseDTO getExpense() {
    return expense;
  }

  public void setExpense(ExpenseDTO expense) {
    this.expense = expense;
  }
}
