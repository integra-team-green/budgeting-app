package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// varianta buna
public class PaymentMapper {

  /**
   * Map Payment to PaymentDTO
   *
   * @param payment
   * @return new PaymentDTO
   */
  public static PaymentDTO getDTO(Payment payment) {
    if (payment == null) return null;

    Long id = payment.getId();
    String name = payment.getName();
    Payment.StatusEnum status = payment.getStatus();
    LocalDate paymentDate = payment.getPaymentDate();
    BigDecimal amount = payment.getAmount();

    ExpenseDTO expenseDto = null;
    if (payment.getExpense() != null) {
      cloudflight.integra.backend.entity.Expense expense = payment.getExpense();
      expenseDto = new ExpenseDTO();
      expenseDto.setId(expense.getId());
      expenseDto.setCategory(expense.getCategory());
      expenseDto.setAmount(expense.getAmount());
      expenseDto.setDate(expense.getDate());
    }

    return new PaymentDTO(id, expenseDto, name, amount, status, paymentDate);
  }

  /**
   * Map PaymentDTO to Payment
   *
   * @param paymentDTO
   * @return new Payment object
   */
  public static Payment getFromDTO(PaymentDTO paymentDTO, ExpenseRepository expenseRepo) {
    if (paymentDTO == null) return null;

    Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    payment.setName(paymentDTO.getName());
    payment.setStatus(paymentDTO.getStatus());
    payment.setPaymentDate(paymentDTO.getPaymentDate());

    if (paymentDTO.getAmount() == null) {
      throw new IllegalArgumentException("Payment amount cannot be null");
    }
    payment.setAmount(paymentDTO.getAmount());

    // map ExpenseDTO -> Expense entity
    if (paymentDTO.getExpense() != null && paymentDTO.getExpense().getId() != null) {
      payment.setExpense(
          expenseRepo
              .findById(paymentDTO.getExpense().getId())
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "Expense not found with id: " + paymentDTO.getExpense().getId())));
    } else {
      throw new IllegalArgumentException("Payment must be linked to an Expense");
    }

    return payment;
  }

  /** Map a list of PaymentDTOs into a list of Payments */
  public static List<Payment> getPaymentsFromDto(
      List<PaymentDTO> paymentDTOList, ExpenseRepository expenseRepo) {
    if (paymentDTOList == null) return List.of();
    return paymentDTOList.stream().map(dto -> getFromDTO(dto, expenseRepo)).toList();
  }

  /** Map a list of Payments into a list of PaymentDTOs */
  public static List<PaymentDTO> getPaymentDTOsFromPayments(List<Payment> payments) {
    if (payments == null) return List.of();
    return payments.stream().map(PaymentMapper::getDTO).toList();
  }
}
