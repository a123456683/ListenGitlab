package com.mindlinker.listengitlab.untis.CompareBranchUtils.Impl;

import com.mindlinker.listengitlab.strategy.CompareBranchStrategy;
import com.mindlinker.listengitlab.untis.CompareBranchUtils.CompareBranchUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CompareGitlabBranchUtilsImpl implements CompareBranchUtils {

    @Override
    public boolean compareBranchIsDiff(String projectId, String commitId, CompareBranchStrategy compareBranchStrategy) throws IOException {
        return compareBranchStrategy.compareBranchIsDiff(projectId, commitId);
    }
}
