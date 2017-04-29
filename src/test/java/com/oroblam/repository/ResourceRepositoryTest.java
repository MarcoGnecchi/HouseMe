package com.oroblam.repository;

import com.oroblam.AddResourceException;
import com.oroblam.model.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.*;

public class ResourceRepositoryTest {

    ResourceRepository resourceRepository;

    public static final Path TEST_WORKING_DIRECTORY = Paths.get("/Users/magnecch/monitor_test");


    @Before
    public void setUp() throws IOException {
        Files.createDirectories(TEST_WORKING_DIRECTORY);
        Files.walk(TEST_WORKING_DIRECTORY, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .peek(System.out::println)
                .forEach(File::delete);
        resourceRepository = new ResourceRepository();
        ReflectionTestUtils.setField(resourceRepository,"WORKING_DIRECTORY", TEST_WORKING_DIRECTORY.toString());
    }

    @Test
    public void testAdd() throws Exception, AddResourceException {
        Resource resource = new Resource();
        Path path = Paths.get(TEST_WORKING_DIRECTORY.toString(), String.valueOf(resource.hashCode()));
        resourceRepository.add(resource);
        assertTrue(Files.exists(path));
    }

    @After
    public void tearDown() throws IOException {
        purgeDirectory(TEST_WORKING_DIRECTORY);
    }

    public void purgeDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}