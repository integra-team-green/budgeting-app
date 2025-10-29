package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentMapper {

    /** Entity → DTO */
    public static PaymentDTO getDTO(Payment payment) {
        if (payment == null) return null;

        Long expenseId = null;
        try {
            if (payment.getExpense() != null) {
                expenseId = payment.getExpense().getId();
            }
        } catch (Exception e) {
            expenseId = null;
        }

        return new PaymentDTO(
                payment.getId(), expenseId, payment.getName(), payment.getStatus(), payment.getPaymentDate());
    }

    /** DTO → Entity */
    public static Payment getFromDTO(PaymentDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setName(dto.getName());
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

    /** List<Entity> → List<DTO> */
    public static List<PaymentDTO> getPaymentDTOsFromPayments(List<Payment> payments) {
        if (payments == null) return List.of();
        return payments.stream().map(PaymentMapper::getDTO).collect(Collectors.toList());
    }

    /** List<DTO> → List<Entity> */
    public static List<Payment> getPaymentsFromDto(List<PaymentDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PaymentMapper::getFromDTO).collect(Collectors.toList());
    }
}
