package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import java.util.List;

public class PaymentMapper {

  /** Map Payment to PaymentDTO */
  public static PaymentDTO getDTO(Payment payment) {
    if (payment == null) return null;

    return new PaymentDTO(
        payment.getId(),
        payment.getExpense() != null ? payment.getExpense().getId() : null,
        payment.getName(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getPaymentDate());
  }

  /** Map PaymentDTO to Payment without repository */
  public static Payment getFromDTO(PaymentDTO dto) {
    if (dto == null) return null;

    Payment payment = new Payment();
    payment.setId(dto.getId());
    payment.setName(dto.getName());
    payment.setAmount(dto.getAmount());
    payment.setStatus(dto.getStatus());
    payment.setPaymentDate(dto.getPaymentDate());

    if (dto.getExpenseId() != null) {
      Expense expense = new Expense();
      expense.setId(dto.getExpenseId());
      payment.setExpense(expense);
    } else {
      throw new IllegalArgumentException("Payment must be linked to an Expense");
    }

    return payment;
  }

  /** Map a list of PaymentDTOs into a list of Payments */
  public static List<Payment> getPaymentsFromDto(List<PaymentDTO> dtos) {
    if (dtos == null) return List.of();
    return dtos.stream().map(PaymentMapper::getFromDTO).toList();
  }

  /** Map a list of Payments into a list of PaymentDTOs */
  public static List<PaymentDTO> getPaymentDTOsFromPayments(List<Payment> payments) {
    if (payments == null) return List.of();
    return payments.stream().map(PaymentMapper::getDTO).toList();
  }
}
