package cloudflight.integra.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Savings")
public class Saving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "goal", nullable = false)
    private String goal;

    @Column(name = "description")
    private String description;

    public Saving(User user, BigDecimal amount, Date date, String goal, String description) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.goal = goal;
        this.description = description;
    }

    public Saving(User user, BigDecimal amount, Date date, String goal) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.goal = goal;
    }

    public Saving(Long id) {
        this.id = id;
    }

    public Saving() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Saving saving = (Saving) o;
        return Objects.equals(id, saving.id)
                && Objects.equals(amount, saving.amount)
                && Objects.equals(date, saving.date)
                && Objects.equals(goal, saving.goal)
                && Objects.equals(description, saving.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, goal, description);
    }

    @Override
    public String toString() {
        return "Saving{" + "id="
                + id + ", user="
                + (user != null ? user.getId() : null) + ", amount="
                + amount + ", date="
                + date + ", goal='"
                + goal + '\'' + ", description='"
                + description + '\'' + '}';
    }

    public void setUserId(Long userId) {}

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
