package com.oroblam.repository;

import com.oroblam.AddResourceException;
import org.springframework.stereotype.Repository;

import com.oroblam.model.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class ResourceRepository {

    public String WORKING_DIRECTORY = "/Users/magnecch/monitor";

    public Integer add(Resource resource) throws AddResourceException {

        try {
            Path file = Files.createDirectories(Paths.get(WORKING_DIRECTORY, String.valueOf(resource.hashCode())));
        } catch (IOException e) {
            throw new AddResourceException();
        }
        return resource.hashCode();
    }
}
