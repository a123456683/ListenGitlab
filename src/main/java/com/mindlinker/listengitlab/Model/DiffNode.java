package com.mindlinker.listengitlab.Model;

import lombok.Data;

@Data
public class DiffNode {
    String old_path;
    String new_path;
    String a_mode;
    String b_mode;
    boolean new_file;
    boolean renamed_file;
    boolean deleted_file;
    String diff;
}
