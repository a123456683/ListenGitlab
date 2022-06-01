package com.mindlinker.listengitlab.model;

import lombok.Data;

@Data
public class DiffNode {

    String oldPath;
    String newPath;
    String aMode;
    String bMode;
    boolean newFile;
    boolean renamedFile;
    boolean deletedFile;
    String diff;
}
