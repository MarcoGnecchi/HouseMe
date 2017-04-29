package com.oroblam.repository;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oroblam.model.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResourceRepositoryTest {

    @Autowired
    public ResourceRepository resourceRepository;

    public static final Path TEST_WORKING_DIRECTORY = Paths.get("/Users/magnecch/monitor_test");

    @Before
    public void setUp() throws IOException {
        Files.createDirectories(TEST_WORKING_DIRECTORY);
        Files.walk(TEST_WORKING_DIRECTORY, FileVisitOption.FOLLOW_LINKS).map(Path::toFile).forEach(File::delete);
        ReflectionTestUtils.setField(resourceRepository, "WORKING_DIRECTORY", TEST_WORKING_DIRECTORY);
    }

    @Test
    public void testAdd() throws Exception {
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
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

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