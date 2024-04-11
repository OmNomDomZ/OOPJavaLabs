package ru.nsu.rabetskii.topic10.task10;

import com.google.common.io.Files;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String folderPath = args[0];
        Iterable<File> files = Files.fileTraverser().depthFirstPostOrder(new File(folderPath));

        System.out.println("Files in folder " + folderPath + ":");
        for (File file : files){
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }
}