package cloudflight.integra.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "User DTO")
public class UserDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Full name", example = "Andrei")
    private String name;

    @Schema(description = "Email address", example = "andrei@gmail.com")
    private String email;

    @Schema(description = "Password", example = "password")
    private String password;

    @Schema(description = "Account creation timestamp ", example = "2025-09-24T14:35:00")
    private LocalDateTime createdAt;

    @Schema(description = "Account balance", example = "1500.75")
    private BigDecimal balance;

    public UserDTO() {}

    public UserDTO(Long id, String name, String email, String password, LocalDateTime createdAt, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
