package cloudflight.integra.backend.mapper;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.repository.ExpenseRepository;
import java.time.LocalDate;
import java.util.List;

public class PaymentMapper {
    /**
     * map Payment to PaymentDTO
     * @param payment
     * @return new PaymentDto
     */
    public static PaymentDTO getDTO(Payment payment) {
        Long id = payment.getId();
        String name = payment.getName();
        String status = payment.getStatus() != null ? payment.getStatus().name() : null; // convert enum -> String
        LocalDate paymentDate = payment.getPaymentDate();
        Long expenseId = payment.getExpense() != null ? payment.getExpense().getId() : null;

        return new PaymentDTO(id, expenseId, name, status, paymentDate);
    }


    /**
     * Map PaymentDto to Payment
     * @param paymentDTO
     * @return new Payment object
     */
    public static Payment getFromDTO(PaymentDTO paymentDTO, ExpenseRepository expenseRepo) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setName(paymentDTO.getName());
        payment.setStatus(paymentDTO.getStatus() != null ? Payment.Status.valueOf(paymentDTO.getStatus()) : null); // String -> enum
        payment.setPaymentDate(paymentDTO.getPaymentDate());

        if (paymentDTO.getExpenseId() != null) {
            payment.setExpense(expenseRepo.getReferenceById(paymentDTO.getExpenseId()));
        }

        return payment;
    }


    /**
     * Map a list of Dtos into a list of objects
     * @param paymentDTOList
     * @return list of objects
     */

    public static List<Payment> getPaymentsFromDto(List<PaymentDTO> paymentDTOList) {
        return paymentDTOList.stream().map(PaymentMapper::getFromDTO).toList();
    }

    /**
     * Map a list of objects into a list of dtos
     * @param payments
     * @return list of dtos
     */
    public static List<PaymentDTO> getPaymentDTOsFromPayments(List<Payment> payments) {

        return payments.stream().map(PaymentMapper::getDTO).toList();
    }
}
