package com.mindlinker.listengitlab.untis.CompareBranchUtils;

import com.mindlinker.listengitlab.strategy.CompareBranchStrategy;

import java.io.IOException;

public interface CompareBranchUtils {
    boolean compareBranchIsDiff(String projectId, String commitId, CompareBranchStrategy compareBranchStrategy) throws IOException;

}
