package com.mindlinker.listengitlab.Untis.Impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindlinker.listengitlab.Model.DiffNode;
import com.mindlinker.listengitlab.Properties.GitlabProperties;
import com.mindlinker.listengitlab.Untis.CompareBranchUtils;
import com.mindlinker.listengitlab.Untis.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class CompareBranchUtilsImpl implements CompareBranchUtils {

    @Autowired
    GitlabProperties gitlabProperties;

    @Override
    public boolean compareBranchIsDiff(String projectId, String gitCommitId) throws IOException {

        String gitlabAgreement = gitlabProperties.getAgreement();
        String gitlabAddress = gitlabProperties.getAddress();
        String commandFirst = "/api/v4/projects/";
        String commandSecond = "/repository/compare?";
        String targetBranch = URLEncoder.encode(gitlabProperties.getTargetBranch(), "UTF-8").replace("+", "%20");
        gitlabProperties.getTargetBranch();
        String tokenKey = gitlabProperties.getTokenKey();
        String tokenValue = gitlabProperties.getTokenValue();

        String compareUrl = gitlabAgreement + "://" + gitlabAddress + commandFirst + projectId + commandSecond + "from=" + gitCommitId + "&to=" + targetBranch;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, Object> httpHead = new HashMap<>();
        httpHead.put(tokenKey, tokenValue);
        String resp = HttpUtils.sendGetRequest(compareUrl, httpHead);
        JsonNode jsonNode = mapper.readValue(resp, JsonNode.class);
        JsonNode diffNode = jsonNode.get("diffs");
        DiffNode[] diffNodeList = mapper.treeToValue(diffNode, DiffNode[].class);
        if (diffNodeList != null && diffNodeList.length > 0) {
            return true;
        } else {
            return false;
        }
    }
}
