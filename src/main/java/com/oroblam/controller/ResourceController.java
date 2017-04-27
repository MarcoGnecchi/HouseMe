package com.oroblam.controller;

import com.oroblam.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oroblam.model.Resource;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @RequestMapping(value = "/status")
    String getStatus() {
        return "OK";
    }

    @RequestMapping(value = "/monitor", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody Resource resource) {
        Integer id = resourceRepository.add(resource);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }
}
