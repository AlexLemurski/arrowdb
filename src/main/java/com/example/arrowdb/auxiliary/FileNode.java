package com.example.arrowdb.auxiliary;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileNode {

    private final String name;
    private final String path;
    private final boolean isDirectory;

    public FileNode(String name, String path, boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
    }
}