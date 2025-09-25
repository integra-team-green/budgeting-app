package cloudflight.integra.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(
      name = "expense_id",
      nullable = false) // Asigură că fiecare Payment trebuie să aibă un Expense
  private Expense expense;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  private StatusEnum status; // pending, paid, failed

  private LocalDate paymentDate;

  @PositiveOrZero
  @Column(nullable = false)
  private BigDecimal amount;

  // Enum pentru status
  public enum StatusEnum {
    PENDING,
    PAID,
    FAILED
  }

  public Payment(Expense expense, String name, StatusEnum status, LocalDate paymentDate) {
    this.expense = expense;
    this.name = name;
    this.status = status;
    this.paymentDate = paymentDate;
  }

  public Payment() {}

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

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }
}
