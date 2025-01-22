package com.qre.tg.dao.appsource;

import com.qre.tg.entity.ticket.TicketIssuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSourceRepository extends JpaRepository<TicketIssuer, Integer> {
    TicketIssuer findByIssuerId(Integer issuerId);

}
