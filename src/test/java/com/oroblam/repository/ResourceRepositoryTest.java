package com.oroblam.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oroblam.model.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResourceRepositoryTest {

    @Autowired
    public ResourceRepository resourceRepository;


    @Value("${working.directory}")
    private String workingDirectory;

    @Before
    public void setUp() throws IOException {
        Path workingPath = Paths.get(workingDirectory);
        Files.createDirectories(workingPath);
        Files.walk(workingPath, FileVisitOption.FOLLOW_LINKS).map(Path::toFile).forEach(File::delete);
        ReflectionTestUtils.setField(resourceRepository, "WORKING_DIRECTORY", workingPath);
    }

    @Test
    public void testAdd() throws Exception {
        Resource resource = new Resource();
        Path path = Paths.get(workingDirectory, String.valueOf(resource.hashCode()) + ".json");
        resourceRepository.add(resource);
        assertTrue(path + " do not exist", Files.exists(path));
    }

    @Test
    public void testUpdate() throws Exception {
        Resource resource = new Resource("foo.com");
        Path path = Paths.get(workingDirectory, String.valueOf(resource.hashCode()) + ".json");
        Integer id = resourceRepository.add(resource);
        assertNull(resource.getContent());
        resource.setContent("bar");
        //Updating
        resourceRepository.update(resource);

        assertThat(resourceRepository.get(resource).getContent(), is("bar"));
    }

    @After
    public void tearDown() throws IOException {
        purgeDirectory(Paths.get(workingDirectory));
    }

    public void purgeDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.getFileName().endsWith(".json")){
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}