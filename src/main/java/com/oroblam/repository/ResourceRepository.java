package com.oroblam.repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oroblam.model.Resource;

@Component
public class ResourceRepository {

    @Autowired
    private ObjectMapper mapper;

    DirectoryStream.Filter<Path> filter = file -> file.getFileName().toString().endsWith(".json");

    private Logger log = LoggerFactory.getLogger(ResourceRepository.class);

    public static Path WORKING_DIRECTORY = Paths.get("/Users/magnecch/monitor");

    public Integer add(Resource resource) throws AddResourceException {
        try {
            //Create working directory if does NOT exist
            if (!Files.exists(WORKING_DIRECTORY)) {
                Files.createDirectory(WORKING_DIRECTORY);
            }
            String fileName = String.valueOf(resource.hashCode());
            Path file = Files
                    .createFile(Paths.get(WORKING_DIRECTORY.toString(), fileName + ".json"));
            byte[] object = mapper.writeValueAsBytes(resource);
            Files.write(file, object);
        } catch (FileAlreadyExistsException e){
            return resource.hashCode();
        } catch (IOException e) {
            throw new AddResourceException(e);
        }

        return resource.hashCode();
    }

    public void update(Resource resource) {
        String fileName = String.valueOf(resource.hashCode());
        Path file = null;
        try {
            byte[] object = mapper.writeValueAsBytes(resource);
            Files.write(Paths.get(WORKING_DIRECTORY.toString(), fileName + ".json"), object);
        } catch (IOException e) {
            log.error("error uploading file");
        }
    }

    public List<Resource> getAll() {

        List<Resource> resources = new ArrayList<>();
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(WORKING_DIRECTORY, filter);
            paths.forEach(path -> {
                try {
                    Resource resource = mapper.readValue(path.toFile(), Resource.class);
                    resources.add(resource);
                } catch (IOException | IllegalArgumentException e) {
                    log.error("Error while reading resource:{}", path.getFileName(), e);
                }
            });
        } catch (IOException e) {
            log.error("Error retrieving resources");
        }
        return resources;
    }

    public Resource get(Resource resource) {
        String fileName = String.valueOf(resource.hashCode());
        byte[] bytes = new byte[0];
        Resource savedResource = null;
        try {
            bytes = Files.readAllBytes(Paths.get(WORKING_DIRECTORY.toString(), fileName + ".json"));
            savedResource = mapper.readValue(bytes, Resource.class);
        } catch (IOException e) {
            log.error("error getting resource");
            throw new IllegalArgumentException();
        }

        return savedResource;
    }

}
