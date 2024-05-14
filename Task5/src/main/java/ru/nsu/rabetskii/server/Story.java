package ru.nsu.rabetskii.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

class Story {

    private final LinkedList<String> story = new LinkedList<>();

    public void addStoryEl(String el) {
        if (story.size() >= 10) {
            story.removeFirst();
            story.add(el);
        } else {
            story.add(el);
        }
    }
    public void printStory(BufferedWriter writer) {
        if(!story.isEmpty()) {
            try {
                writer.write("History messages" + "\n");
                for (String message : story) {
                    writer.write(message + "\n");
                }
                writer.write("/...." + "\n");
                writer.flush();
            } catch (IOException ignored) {}
        }
    }
}
