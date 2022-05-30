package com.mindlinker.listengitlab.Untis;

import java.io.UnsupportedEncodingException;

public interface MergeRequestUtils {
    String createMergeRequest(String projectId, String gitCommitId, int assigneeId) throws UnsupportedEncodingException;
}
