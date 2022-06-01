package com.mindlinker.listengitlab.untis.MergeRequestUtils.Impl;

import com.mindlinker.listengitlab.properties.CreateMRProperties;
import com.mindlinker.listengitlab.properties.GitlabProperties;
import com.mindlinker.listengitlab.untis.HttpUtils;
import com.mindlinker.listengitlab.untis.MergeRequestUtils.MergeRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GitlabMergeRequestUtils implements MergeRequestUtils {

    @Autowired
    GitlabProperties gitlabProperties;
    @Autowired
    CreateMRProperties createMRProperties;

    @Override
    public String createMergeRequest(String projectId, String gitBranch, int assigneeId){
        log.debug(this.getClass().getName() + ".createMergeRequest(): method begin, and projectId = " + projectId + "and gitBranch = " + gitBranch + "and assigneeId = " + assigneeId);

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
        paramesMap.put("id", projectId);
        paramesMap.put("source_branch", gitBranch);
        paramesMap.put("target_branch", targetBranch);
        paramesMap.put("title", mergeRequestTitle);
        paramesMap.put("assignee_id", assigneeId);

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);

        log.debug(this.getClass().getName() + ".createMergeRequest(): Prepare for create MergeRequest");
        String resp = HttpUtils.sendPostRequest(createMRUrl, paramesMap, httpHead);
        log.debug(this.getClass().getName() + ".createMergeRequest(): resp = " + resp);
        return resp;
    }
}
