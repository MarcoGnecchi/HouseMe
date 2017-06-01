package com.oroblam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebComparatorService {

    private static Logger log = LoggerFactory.getLogger(WebComparatorService.class);

    /**
     * Compares web request content
     * @param savedContent
     * @param newContent
     * @return true whe a difference is detected
     */
    public boolean isUpdated(String savedContent, String newContent) throws IllegalArgumentException {

        if (savedContent == null || newContent == null) {
            log.error("Cannot check null elements");
            throw new IllegalArgumentException("Cannot check null element");
        }
        return !savedContent.equals(newContent);
    }
}
