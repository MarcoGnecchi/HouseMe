package com.oroblam.controller;

import com.oroblam.model.Resource;
import com.oroblam.service.MonitorService;
import com.oroblam.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExecutorController{

    @Autowired
    MonitorService monitorService;

    @Autowired
    NotificationService notificationService;

    Logger log = LoggerFactory.getLogger(ExecutorController.class);

    @RequestMapping(value = "/check")
    public void triggerChecks(){
        log.info("Running checks");
        List<Resource> updateResources = monitorService.run();
        updateResources.forEach(resource -> notificationService.notify(resource));
    }
}
