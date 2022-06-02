package com.mindlinker.listengitlab.untis.MergeRequestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MergeRequestUtils {
    String createMergeRequest(String projectId, String gitCommitId, int assigneeId);

    boolean judgeCreateMRsuccessfully(String result) throws JsonProcessingException;
}
