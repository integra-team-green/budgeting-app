package cloudflight.integra.backend.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


public class SavingDTO {
    private Long id;
    private BigDecimal amount;
    private Date date;
    private String goal;
    private String description;

    public SavingDTO(Long id, BigDecimal amount, Date date, String goal, String description) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.goal = goal;
        this.description = description;
    }

    public SavingDTO(BigDecimal amount, Date date, String goal, String description) {
        this.amount = amount;
        this.date = date;
        this.goal = goal;
        this.description = description;
    }

    public SavingDTO(){

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
        SavingDTO savingDTO = (SavingDTO) o;
        return Objects.equals(id, savingDTO.id) && Objects.equals(amount, savingDTO.amount) && Objects.equals(date, savingDTO.date) && Objects.equals(goal, savingDTO.goal) && Objects.equals(description, savingDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, goal, description);
    }

    @Override
    public String toString() {
        return "SavingDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", goal='" + goal + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
