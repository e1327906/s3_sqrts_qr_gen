package com.qre.tg.dao.ticket;

import com.qre.tg.entity.ticket.TicketValidityDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketValidityDomainRepository extends JpaRepository<TicketValidityDomain, UUID> {
}
