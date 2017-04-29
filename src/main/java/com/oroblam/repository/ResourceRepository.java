package com.oroblam.repository;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oroblam.AddResourceException;
import com.oroblam.model.Resource;

@Component
public class ResourceRepository {

    @Autowired
    private ObjectMapper mapper;

    private Logger log = LoggerFactory.getLogger(ResourceRepository.class);

    public Path WORKING_DIRECTORY = Paths.get("/Users/magnecch/monitor");

    public Integer add(Resource resource) throws AddResourceException {
        try {
            //Create working directory if does NOT exist
            if (!Files.exists(WORKING_DIRECTORY)){
                Files.createDirectory(WORKING_DIRECTORY);
            }
            Path file = Files
                    .createFile(Paths.get(WORKING_DIRECTORY.toString(), String.valueOf(resource.hashCode())));
            byte[] object = mapper.writeValueAsBytes(resource);
            Files.write(file, object);
        } catch (IOException e) {
            throw new AddResourceException(e);
        }
        return resource.hashCode();
    }

    public void update(Resource resource) {

    }

    public List<Resource> getAll() {

        List<Resource> resources = new ArrayList<>();
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(WORKING_DIRECTORY);
            paths.forEach(path -> {
                try {
                    Resource resource = mapper.readValue(path.toFile(), Resource.class);
                    resources.add(resource);
                } catch (IOException e) {
                    log.error("Error while reading resource:{}", path.getFileName());
                }
            });
        } catch (IOException e) {
            log.error("Error retrieving resources");
        }
        return resources;
    }

    public Resource get() {
        return new Resource();
    }
}
