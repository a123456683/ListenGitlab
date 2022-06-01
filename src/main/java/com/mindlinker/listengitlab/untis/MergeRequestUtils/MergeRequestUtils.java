package com.mindlinker.listengitlab.untis.MergeRequestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MergeRequestUtils {
    String createMergeRequest(String projectId, String gitCommitId, int assigneeId) throws UnsupportedEncodingException, IOException;
}
