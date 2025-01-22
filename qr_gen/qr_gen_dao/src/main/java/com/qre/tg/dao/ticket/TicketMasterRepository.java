package com.qre.tg.dao.ticket;

import com.qre.tg.entity.ticket.TicketMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketMasterRepository extends JpaRepository<TicketMaster, UUID> {

    List<TicketMaster> findAllByEmail(String email);

    Optional<TicketMaster> findAllBySerialNumber(String serialNumber);

    /*@Query("SELECT t FROM TicketMaster t WHERE t.transactionData.paymentRefNo = :paymentRefNo")
    Optional<TicketMaster> findByTransactionPaymentRefNo(String paymentRefNo);*/

    Optional<TicketMaster> findByTransactionDataPaymentRefNo(String paymentRefNo);
}
