package com.qre.tg.entity.ticket;


import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.TICKET_MASTER,
        indexes = {
        @Index(name="pno_email_idx",columnList = "serial_number,phone_no,email", unique = true),
        @Index(name="payment_ref_no_idx",columnList = "payment_ref_no", unique = true)
})
public class TicketMaster extends AbstractPersistableEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DbFieldName.TICKET_ID)
    private UUID ticketId;

    @Column(name = DbFieldName.TICKET_TYPE)
    private Integer ticketType;

    @Column(name = DbFieldName.JOURNEY_TYPE_ID)
    private Integer journeyTypeId;

    @Column(name = DbFieldName.SERIAL_NUMBER)
    private String serialNumber;

    @Column(name = DbFieldName.GROUP_SIZE)
    private int groupSize;

    @Column(name = DbFieldName.CREATOR_ID)
    private int creatorId;

    @Column(name = DbFieldName.VALIDITY_PERIOD)
    private int validityPeriod;

    @Column(name = DbFieldName.OPERATOR_ID)
    private int operatorId;

    @Column(name = DbFieldName.CREATION_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    Date creationDateTime;

    @Column(name = DbFieldName.EFFECTIVE_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    Date effectiveDateTime;

    @Column(name = DbFieldName.ISSUER_ID)
    private int issuerId;

    @Column(name = DbFieldName.VALIDITY_DOMAIN)
    private int validityDomain;

    @Column(name = DbFieldName.USER_ID)
    private UUID userId;

    @Column(name = DbFieldName.PHONE_NO)
    private String phoneNo;

    @Column(name = DbFieldName.EMAIL)
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = DbFieldName.TICKET_ID)
    private List<JourneyDetails> journeyDetails;

    @Column(name = DbFieldName.QR_DATA)
    private byte[] qrData;

    @Embedded
    private TransactionData transactionData;

    @Embedded
    private Security security;

    @Embedded
    private AdditionalInfo additionalInfo;

    @Override
    public UUID getId() {
        return ticketId;
    }
}
