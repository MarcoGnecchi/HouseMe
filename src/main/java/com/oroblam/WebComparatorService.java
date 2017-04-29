package com.oroblam;

import org.springframework.stereotype.Component;

@Component
public class WebComparatorService {

    /**
     * Compares web request content
     * @param savedContent
     * @param newContent
     * @return true whe a difference is detected
     */
    public boolean compare(String savedContent, String newContent) {
        return false;
    }
}
