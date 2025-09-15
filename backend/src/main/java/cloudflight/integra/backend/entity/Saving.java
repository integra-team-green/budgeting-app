package cloudflight.integra.backend.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Saving {
    private Long id;
    private BigDecimal amount;
    private Date date;
    private String goal;
    private String description;

    public Saving(Long id, BigDecimal amount, Date date, String goal, String description) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.goal = goal;
        this.description = description;
    }

    public Saving(Long id, BigDecimal amount, Date date, String goal) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.goal = goal;
    }

    public Saving(Long id) {
        this.id = id;
    }

    public Saving() {

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
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
        Saving saving = (Saving) o;
        return Objects.equals(id, saving.id) && Objects.equals(amount, saving.amount) && Objects.equals(date, saving.date) && Objects.equals(goal, saving.goal) && Objects.equals(description, saving.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, goal, description);
    }

    @Override
    public String toString() {
        return "Saving{" +
            "id=" + id +
            ", amount=" + amount +
            ", date=" + date +
            ", goal='" + goal + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
