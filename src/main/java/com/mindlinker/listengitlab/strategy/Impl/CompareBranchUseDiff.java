package com.mindlinker.listengitlab.strategy.Impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mindlinker.listengitlab.model.DiffNode;
import com.mindlinker.listengitlab.properties.GitlabProperties;
import com.mindlinker.listengitlab.strategy.CompareBranchStrategy;
import com.mindlinker.listengitlab.untis.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CompareBranchUseDiff implements CompareBranchStrategy {

    @Autowired
    GitlabProperties gitlabProperties;

    @Override
    public boolean compareBranchIsDiff(String projectId, String gitBranch) throws JsonProcessingException, UnsupportedEncodingException {
        log.info("compareBranchIsDiff(): method begin, and projectId = " + projectId + " and gitBranch = " + gitBranch);

        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String commandSecond = "/repository/compare?";
        String targetBranch = URLEncoder.encode(gitlabProperties.getTargetBranch(), "UTF-8").replace("+", "%20");
        gitBranch = URLEncoder.encode(gitBranch, "UTF-8").replace("+", "%20");
        gitlabProperties.getTargetBranch();
        String tokenKey = gitlabProperties.getTokenKey();
        String tokenValue = gitlabProperties.getTokenValue();

        String compareUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond + "from=" + targetBranch + "&to=" + gitBranch;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);

        log.debug("compareBranchIsDiff(): Prepare for comparison");
        String resp = HttpUtils.sendGetRequest(compareUrl, httpHead);
        log.debug("compareBranchIsDiff(): resp = " + resp);

        JsonNode jsonNode = mapper.readValue(resp, JsonNode.class);
        JsonNode diffNode = jsonNode.get("diffs");

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DiffNode[] diffNodeList = mapper.treeToValue(diffNode, DiffNode[].class);
        if (diffNodeList != null && diffNodeList.length > 0) {
            log.info("Two branches are different, prepare to create MergeRequest");
            return true;
        } else {
            log.info("Two branches are the same, don't create MergeRequest");
            return false;
        }
    }
}
