package com.oroblam.controller;

import com.oroblam.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExecutorController{

    @Autowired
    MonitorService monitorService;

    Logger log = LoggerFactory.getLogger(ExecutorController.class);

    @RequestMapping(value = "/check")
    public void triggerChecks(){
        log.info("Running checks");
        monitorService.run();
    }
}
