package com.qre.tg.query.api.service.impl;

import com.qre.tg.query.api.common.JourneyTypeEnum;
import com.qre.tg.query.api.common.TransportMode;
import org.springframework.stereotype.Component;

@Component
public class FareStrategyFactory {

    private final SingleJourneyStrategy singleJourneyStrategy;
    private final GroupFareStrategy groupFareStrategy;
    private final ReturnJourneyStrategy returnJourneyStrategy;

    public FareStrategyFactory(SingleJourneyStrategy singleJourneyStrategy,
                               GroupFareStrategy groupFareStrategy,
                               ReturnJourneyStrategy returnJourneyStrategy) {
        this.singleJourneyStrategy = singleJourneyStrategy;
        this.groupFareStrategy = groupFareStrategy;
        this.returnJourneyStrategy = returnJourneyStrategy;
    }

    public TicketTypeStrategy getStrategy(JourneyTypeEnum ticketType, TransportMode mode) {
        switch (ticketType) {
            case SINGLE:
                singleJourneyStrategy.setTicketTypeStrategy(mode);
                return singleJourneyStrategy;
            case GROUP:
                groupFareStrategy.setTicketTypeStrategy(mode);
                return groupFareStrategy;
            case RETURN_TICKET:
                returnJourneyStrategy.setTicketTypeStrategy(mode);
                return returnJourneyStrategy;
            default:
                throw new IllegalArgumentException("Unknown ticket type: " + ticketType);
        }
    }
}
