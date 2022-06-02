package com.mindlinker.listengitlab.untis.BranchUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface BranchUtils {

    boolean createRepositoryBranch(String projectId, String branchName, String commitId) throws JsonProcessingException;

    boolean deleteRepositoryBranch(String projectId, String branchName);

    boolean deleteAllMergedBranch(String projectId);
}
