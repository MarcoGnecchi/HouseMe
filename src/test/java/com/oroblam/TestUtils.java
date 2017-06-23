package com.oroblam;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by magnecch on 22/06/2017.
 */
public class TestUtils {

    public static void purgeDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.getFileName().toString().endsWith(".json")){
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
