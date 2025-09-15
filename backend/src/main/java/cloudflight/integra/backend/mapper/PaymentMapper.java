package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PaymentMapper {
    /**
     * map Payment to PaymentDTO
     * @param payment
     * @return new PaymentDto
     */
    public static PaymentDTO getDTO(Payment payment) {
        String nume= payment.getName();
        Long id= payment.getId();
        BigDecimal amount= payment.getAmount();
        Date nextDueDate= payment.getNextDueDate();
        Frequency frequency= payment.getFrequency();
        Boolean isActive= payment.getIsActive();
        return new PaymentDTO(id,nume,amount,frequency,nextDueDate,isActive);
    }

    /**
     * Map PaymentDto to Payment
     * @param paymentDTO
     * @return new Payment object
     */
    public static Payment getFromDTO(PaymentDTO paymentDTO) {
        String nume= paymentDTO.getName();
        Long id= paymentDTO.getId();
        BigDecimal amount= paymentDTO.getAmount();
        Date nextDueDate= paymentDTO.getNextDueDate();
        Frequency frequency= paymentDTO.getFrequency();
        Boolean isActive= paymentDTO.getIsActive();
        return new Payment(id,nume,amount,frequency,nextDueDate,isActive);
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
