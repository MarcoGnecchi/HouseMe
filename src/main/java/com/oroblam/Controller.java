package com.oroblam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/status")
    String getStatus() {
        return "OK";
    }
}
