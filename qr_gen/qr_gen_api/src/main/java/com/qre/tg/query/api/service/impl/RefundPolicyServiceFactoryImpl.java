package com.qre.tg.query.api.service.impl;

import com.qre.tg.query.api.common.RefundPolicyTypeEnum;
import com.qre.tg.query.api.service.RefundPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefundPolicyServiceFactoryImpl {

    private final FullRefundPolicyServiceImpl fullRefundPolicyServiceImpl;

    private final PartialRefundPolicyServiceImpl partialRefundPolicyServiceImpl;

    public RefundPolicyService createRefundPolicy(RefundPolicyTypeEnum policyType) {
        switch (policyType) {
            case FULL_REFUND:
                return fullRefundPolicyServiceImpl;
            case PARTIAL_REFUND:
                return partialRefundPolicyServiceImpl;
            default:
                throw new IllegalArgumentException("Invalid policy type: " + policyType);
        }
    }

    public long hoursDifference(Date effectiveDateTime) {
        Instant ticketInstant = effectiveDateTime.toInstant();
        Instant nowInstant = Instant.now();
        return Duration.between(ticketInstant, nowInstant).toHours();
    }
}