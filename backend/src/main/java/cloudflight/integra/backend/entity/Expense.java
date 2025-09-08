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


/** Represents a financial expense entry for a specific user.**/
public class Expense {

    /** Unique identifier of the expense. */
    private Long id;

    /** Identifier of the user who created the expense. */
    private Long userId;

    /** Monetary value of the expense. */
    private BigDecimal amount;

    /** Category of the expense. */
    private String category;

    /** Date when the expense occurred. */
    private LocalDate date;

    /** Optional description with extra details about the expense. */
    private String description;

    /**
     * Full constructor for creating an {@code Expense} instance.
     *
     * @param id          unique identifier of the expense
     * @param userId      identifier of the user who created it
     * @param amount      monetary value of the expense
     * @param category    category of the expense
     * @param date        date when the expense occurred
     * @param description optional description
     */
   public Expense(Long id, Long userId, BigDecimal amount, String category, LocalDate date, String description) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    /** @return unique identifier of the expense */
    public Long getId() {

        return id;
   }

    /** @param id unique identifier of the expense */
    public void setId(Long id) {

        this.id = id;
   }

    /** @return user identifier */
    public Long getUserId() {

        return userId;
   }

    /** @param userId identifier of the user who created the expense */
    public void setUserId(Long userId) {

        this.userId = userId;
   }

    /** @return monetary value of the expense */
    public BigDecimal getAmount() {

        return amount;
   }

    /** @param amount monetary value of the expense */
    public void setAmount(BigDecimal amount) {

        this.amount = amount;
   }

    /** @return category of the expense */
    public String getCategory() {

        return category;
   }

    /** @param category category of the expense */
    public void setCategory(String category) {

        this.category = category;
   }

    /** @return date when the expense occurred */
    public LocalDate getDate() {
       return date;
   }

    /** @param date date when the expense occurred */
    public void setDate(LocalDate date) {

        this.date = date;
   }

    /** @return optional description of the expense */
    public String getDescription() {

        return description;
   }

    /** @param description optional description of the expense */
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


    /** @return string representation of the expense for logging/debugging */
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
