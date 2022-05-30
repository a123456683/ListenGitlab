package com.mindlinker.listengitlab.Untis;

import java.io.IOException;

public interface CompareBranchUtils {
    boolean compareBranchIsDiff(String projectId, String gitCommitId) throws IOException;
}
