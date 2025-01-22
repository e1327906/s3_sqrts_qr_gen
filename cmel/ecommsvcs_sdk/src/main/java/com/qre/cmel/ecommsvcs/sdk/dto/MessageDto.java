package com.qre.cmel.ecommsvcs.sdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto implements Serializable {
    /**
     * from
     */
    private String from;

    /**
     * to
     */
    private String to;

    /**
     * message
     */
    private String message;

    /**
     * subject
     */
    private String subject;

    /**
     * cc
     */
    private String cc;

    /**
     * bcc
     */
    private String bcc;

    /**
     * filePath
     */
    private byte[] imageBytes;

    /**
     * fileName
     */
    private String fileNameWithExtension;
}
