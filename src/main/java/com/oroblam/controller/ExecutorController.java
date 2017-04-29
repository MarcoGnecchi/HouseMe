package com.oroblam.controller;

import com.oroblam.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExecutorController{

    @Autowired
    MonitorService monitorService;

    @RequestMapping(value = "/api/check")
    public void triggerChecks(){
        monitorService.run();
    }


}
