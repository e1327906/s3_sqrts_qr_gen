
package com.qre.cmel.ecommsvcs.sdk.service;


import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import org.springframework.lang.NonNull;

import javax.mail.MessagingException;
import java.io.IOException;

public interface ECommSvcService {
    void send(@NonNull MessageDto messageDto) throws MessagingException, IOException;
}
