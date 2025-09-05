package cloudflight.integra.backend.model;




import java.math.BigDecimal;
import java.util.Date;



public class Payment {
    private Long id;
    private String name;
    private BigDecimal amount;
    private Frequency frequency;
    private Date nextDueDate;
    private Boolean isActive;
    public Payment(Long id, String name, BigDecimal amount,Frequency frequency,Date nextDueDate, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.frequency = frequency;
        this.nextDueDate = nextDueDate;
        this.isActive = isActive;
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
    public Frequency getFrequency() {
        return frequency;
    }
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;

    }
    public Date getNextDueDate() {
        return nextDueDate;

    }
    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
