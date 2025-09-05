package cloudflight.integra.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/*As a user, I want to manage my expenses so that I can track all my spending.
*
* Fields:
    id: Long
    userId: Long (FK → User)
    amount: BigDecimal
    category: String
    date: Date
    description: String (optional)*/

public class Expense {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String description;
   public Expense(Long id, Long userId, BigDecimal amount, String category, LocalDate date, String description) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public Long getId() {
       return id;
   }
    public void setId(Long id) {
       this.id = id;
   }

    public Long getUserId() {
       return userId;
   }
    public void setUserId(Long userId) {
       this.userId = userId;
   }

    public BigDecimal getAmount() {
       return amount;
   }
    public void setAmount(BigDecimal amount) {
       this.amount = amount;
   }

    public String getCategory() {
       return category;
   }
    public void setCategory(String category) {
       this.category = category;
   }

    public LocalDate getDate() {
       return date;
   }
    public void setDate(LocalDate date) {
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
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id) && Objects.equals(userId, expense.userId) && Objects.equals(amount, expense.amount) && Objects.equals(category, expense.category) && Objects.equals(date, expense.date) && Objects.equals(description, expense.description);
   }
   @Override
   public int hashCode() {
       return Objects.hash(id, userId, amount, category, date, description);
   }

   @Override
    public String toString() {
       return "Expense {" +
               "Id: " + id +
               "UserId: " + userId +
               "Amount: " + amount +
               "Category: " + category +
               "Date: " + date +
               "Description: " + description;
   }
}
