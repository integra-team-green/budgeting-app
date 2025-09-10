package cloudflight.integra.backend.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


public class Income {
    private Long id;
    private BigDecimal amount;
    private String source;
    private Date date;
    private String description;

    /**
     *
     * @param id generated automatically
     * @param amount the amount of the income
     * @param source the source of the income
     * @param date the date of the income
     * @param description the description of the income(can be optional)
     */
    public Income(Long id, BigDecimal amount, String source, Date date, String description) {
        this.id = id;
        this.amount = amount;
        this.source = source;
        this.date = date;
        this.description = description;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return Objects.equals(id, income.id) && Objects.equals(amount, income.amount) && Objects.equals(source, income.source) && Objects.equals(date, income.date) && Objects.equals(description, income.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, source, date, description);
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", amount=" + amount +
                ", source='" + source + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
