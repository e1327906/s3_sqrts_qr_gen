package com.qre.tg.query.api.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandlerProcessor implements Processor {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerProcessor.class);

    /**
     *
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // the caused by exception is stored in a property on the exchange
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        logger.error("ExceptionHandlerProcessor.process {}", caused.getMessage());
        logger.error("ExceptionHandlerProcessor.process", caused);
    }
}
