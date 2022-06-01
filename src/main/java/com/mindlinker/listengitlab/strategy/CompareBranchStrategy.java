package com.mindlinker.listengitlab.strategy;

import java.io.IOException;

public interface CompareBranchStrategy {
    boolean compareBranchIsDiff(String projectId, String gitCommitId) throws IOException;
}
