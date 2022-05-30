package com.mindlinker.listengitlab.Untis.Impl;

import com.mindlinker.listengitlab.Properties.CreateMRProperties;
import com.mindlinker.listengitlab.Properties.GitlabProperties;
import com.mindlinker.listengitlab.Untis.HttpUtils;
import com.mindlinker.listengitlab.Untis.MergeRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MergeRequestUtilsImpl implements MergeRequestUtils {

    @Autowired
    GitlabProperties gitlabProperties;
    @Autowired
    CreateMRProperties createMRProperties;

    @Override
    public String createMergeRequest(String projectId, String gitBranch, int assigneeId) throws UnsupportedEncodingException {
        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String targetBranch = gitlabProperties.getTargetBranch();
        String tokenValue = gitlabProperties.getTokenValue();
        String tokenKey = gitlabProperties.getTokenKey();


        String commandSecond = "/merge_requests";
        String mergeRequestTitle = createMRProperties.getTitle();
        String createMRUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond;
        Map<String, Object> paramesMap = new HashMap<>();
        paramesMap.put("id", commandFirst);
        paramesMap.put("source_branch", gitBranch);
        paramesMap.put("target_branch", targetBranch);
        paramesMap.put("title", mergeRequestTitle);
        paramesMap.put("assignee_id", assigneeId);

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);
        String resp = HttpUtils.sendPostRequest(createMRUrl, paramesMap, httpHead);
        return resp;
    }
}
