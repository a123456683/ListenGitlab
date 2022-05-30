package com.mindlinker.listengitlab.Listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.mindlinker.listengitlab.Model.DiffNode;
import com.mindlinker.listengitlab.Model.Meta;
import com.mindlinker.listengitlab.Properties.AssigneeProperties;
import com.mindlinker.listengitlab.Properties.CreateMRProperties;
import com.mindlinker.listengitlab.Properties.GitlabProperties;
import com.mindlinker.listengitlab.Properties.ServiceProperties;
import com.mindlinker.listengitlab.Untis.CompareBranchUtils;
import com.mindlinker.listengitlab.Untis.HttpUtils;
import com.mindlinker.listengitlab.Untis.MergeRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ServiceInfoListener {
    @Autowired
    ServiceProperties serviceProperties;
    @Autowired
    AssigneeProperties assigneeProperties;
    @Autowired
    GitlabProperties gitlabProperties;
    @Autowired
    CreateMRProperties createMRProperties;
    @Autowired
    CompareBranchUtils compareBranchUtils;
    @Autowired
    MergeRequestUtils mergeRequestUtils;

    public void listenGitlab() throws IOException, InterruptedException {
        Set<Map.Entry<String, String>> assigneeMapEntries = assigneeProperties.getAssigneeMap().entrySet();

        while (true) {
            for (Map.Entry<String, String> assigneeMapEntry : assigneeMapEntries) {
                // 1.遍历所有配置好的ServiceId
                String serviceId = assigneeMapEntry.getKey();

                // 2. 通过ServiceId去判断当前Service有没有提交信息，有的话则获取最新的提交信息
                Meta newestMeta = getNewestMeta(serviceId);
                if (newestMeta != null) {
                    String commitId = newestMeta.getGitCommitId();
                    String projectId = newestMeta.getProjectId();
                    String gitBranch = newestMeta.getGitBranch();

                    // 3. 检查当前提交的版本和目标版本（主干版本）是否不同
                    if (compareBranchUtils.compareBranchIsDiff(projectId, commitId)) {

                        // 4. 如果两个版本不相同，则发送一个MR
                        mergeRequestUtils.createMergeRequest(projectId, gitBranch, Integer.parseInt(assigneeMapEntry.getValue()));
                    }
                }
            }

            // 5. 每10分钟检查一次
            Thread.sleep(serviceProperties.getCheckTime());
        }
    }

    private Meta getNewestMeta(String serviceId) throws IOException {
        String serviceInfoAgreement = serviceProperties.getAgreement();
        String serviceInfoAddress = serviceProperties.getAddress();
        String serviceInfoPort = serviceProperties.getPort();
        String serviceInfoPrefix = serviceProperties.getPrefix();
        String serviceInfoSuffix = serviceProperties.getSuffix();
        String url = serviceInfoAgreement + "://" + serviceInfoAddress + ":" + serviceInfoPort + serviceInfoPrefix + serviceId + serviceInfoSuffix;
        List<Meta> metaList = new ArrayList<>();

        String resp = HttpUtils.sendGetRequest(url, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode jsonNodeList = mapper.readValue(resp, JsonNode.class);
        for (int i = 0; i < jsonNodeList.size(); i++) {
            JsonNode jsonNode = jsonNodeList.get(i);
            JsonNode serviceNode = jsonNode.get("Service");
            JsonNode metaNode = serviceNode.get("Meta");
            Meta meta = mapper.treeToValue(metaNode, Meta.class);
            if (meta != null && meta.checkMetaIsCommitted()) {
                meta.parseProjectId(gitlabProperties.getAddress());  // 用gitlab域名的名称进行字符串的分离
                metaList.add(meta);
            }
        }
        if (metaList.size() > 0) {
            if (metaList.size() > 1) {
                Collections.sort(metaList, new Comparator<Meta>() {
                    @Override
                    public int compare(Meta meta1, Meta meta2) {
                        return (int) (meta2.getRegisterTimestamp() - meta1.getRegisterTimestamp());
                    }
                });
            }
            Meta newestMeta = metaList.get(0);
            return newestMeta;
        } else {
            return null;
        }
    }
}
