package com.example.demo.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MovieCommands {

    @ShellMethod(key = "search", value = "Movie search")
    public String search(String... movieTitles) {
        StringBuilder name = new StringBuilder();
        for (String word : movieTitles) {
            if (!name.isEmpty()) {
                name.append(" ");
            }
            name.append(word);
        }
        return "I am going to try to search for this movie!: " + name.toString();
    }
}
