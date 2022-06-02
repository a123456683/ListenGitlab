package com.mindlinker.listengitlab.untis.BranchUtils.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindlinker.listengitlab.properties.GitlabProperties;
import com.mindlinker.listengitlab.untis.HttpUtils;
import com.mindlinker.listengitlab.untis.BranchUtils.BranchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GitlabRepositoryBranchUtils  implements BranchUtils {
    @Autowired
    GitlabProperties gitlabProperties;

    @Override
    public boolean createRepositoryBranch(String projectId, String branchName, String commitId) throws JsonProcessingException {
        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String tokenValue = gitlabProperties.getTokenValue();
        String tokenKey = gitlabProperties.getTokenKey();

        String commandSecond = "/repository/branches";

        String createMRUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond;
        Map<String, Object> paramesMap = new HashMap<>();

        paramesMap.put("id", projectId);
        paramesMap.put("branch", branchName);
        paramesMap.put("ref", commitId);

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);

        log.debug("createRepositoryBranch(): Prepare to repositoryBranch");
        String resp = HttpUtils.sendPostRequest(createMRUrl, paramesMap, httpHead);
        log.debug("createRepositoryBranch(): resp = " + resp);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = mapper.readValue(resp, JsonNode.class);
        JsonNode nameNode = jsonNode.get("name");
        if (nameNode != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRepositoryBranch(String projectId, String branchName) {
        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String tokenValue = gitlabProperties.getTokenValue();
        String tokenKey = gitlabProperties.getTokenKey();
        String commandSecond = "/repository/branches/";

        String createMRUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond + branchName;
        Map<String, Object> paramesMap = new HashMap<>();

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);

        log.debug("deleteRepositoryBranch(): Prepare to deleteRepositoryBranch");
        String resp = HttpUtils.sendDeleteRequest(createMRUrl, paramesMap, httpHead);
        log.debug("deleteRepositoryBranch(): resp = " + resp);

        return true;
    }

    @Override
    public boolean deleteAllMergedBranch(String projectId) {
        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String tokenValue = gitlabProperties.getTokenValue();
        String tokenKey = gitlabProperties.getTokenKey();
        String commandSecond = "/repository/merged_branches";

        String createMRUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond;

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);

        log.debug("deleteAllMergedBranch(): Prepare to deleteAllMergedBranch");
        String resp = HttpUtils.sendDeleteRequest(createMRUrl, null, httpHead);
        log.debug("deleteAllMergedBranch(): resp = " + resp);

        return true;
    }
}
