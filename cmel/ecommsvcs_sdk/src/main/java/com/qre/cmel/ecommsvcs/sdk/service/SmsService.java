
package com.qre.cmel.ecommsvcs.sdk.service;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import org.springframework.lang.NonNull;


public interface SmsService {
    void send(@NonNull MessageDto messageDto);
}
