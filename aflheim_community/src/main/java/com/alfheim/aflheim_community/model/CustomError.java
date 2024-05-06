package com.alfheim.aflheim_community.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {

    private int errorCode;
    private String errorMessage;
    private Date errorOccurrenceDate;
    private static final Logger logger = LoggerFactory.getLogger(CustomError.class);

    public void logError() {
        logger.error("Error code: {}, Error message: {}, Error time: {}", errorCode, errorMessage, errorOccurrenceDate);
    }
}
