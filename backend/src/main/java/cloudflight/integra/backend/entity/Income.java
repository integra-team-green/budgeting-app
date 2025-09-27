package cloudflight.integra.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Incomes")
public class Income {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "user_id", updatable = false, insertable = false, nullable = false)
  private Long userId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "source", nullable = false)
  private String source;

  @Column(name = "date", nullable = false)
  private Date date;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "frequency", nullable = false)
  private Frequency frequency;

  @Column(name = "end_date")
  private Date endDate;

  /**
   * @param id generated automatically
   * @param amount the amount of the income
   * @param source the source of the income
   * @param date the date of the income
   * @param description the description of the income(can be optional)
   */
  public Income(
      Long id,
      Long userId,
      BigDecimal amount,
      String source,
      Date date,
      String description,
      Frequency frequency,
      Date endDate) {
    this.id = id;
    this.userId = userId;
    this.amount = amount;
    this.source = source;
    this.date = date;
    this.description = description;
    this.frequency = frequency;
    this.endDate = endDate;
  }

  public Income(
      Long id,
      User user,
      BigDecimal amount,
      String source,
      Date date,
      Frequency frequency,
      Date endDate) {
    this.id = id;
    this.user = user;
    this.amount = amount;
    this.source = source;
    this.date = date;
    this.frequency = frequency;
    this.endDate = endDate;
  }

  public Income() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
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

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Income income = (Income) o;
    return Objects.equals(id, income.id)
        && Objects.equals(amount, income.amount)
        && Objects.equals(source, income.source)
        && Objects.equals(date, income.date)
        && Objects.equals(description, income.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, source, date, description);
  }

  @Override
  public String toString() {
    return "Income{"
        + "id="
        + id
        + ", amount="
        + amount
        + ", source='"
        + source
        + '\''
        + ", date="
        + date
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
