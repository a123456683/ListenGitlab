package com.mindlinker.listengitlab.listener;

import com.mindlinker.listengitlab.model.Meta;
import com.mindlinker.listengitlab.properties.AssigneeProperties;
import com.mindlinker.listengitlab.properties.ServiceProperties;
import com.mindlinker.listengitlab.strategy.CompareBranchStrategy;
import com.mindlinker.listengitlab.untis.CompareBranchUtils.CompareBranchUtils;
import com.mindlinker.listengitlab.untis.MergeRequestUtils.MergeRequestUtils;
import com.mindlinker.listengitlab.untis.ServiceMetaUtils.Impl.PraseGitlabServiceMetaUtils;
import com.mindlinker.listengitlab.untis.ServiceMetaUtils.ServiceMetaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class ServiceInfoListener {
    @Autowired
    ServiceProperties serviceProperties;
    @Autowired
    AssigneeProperties assigneeProperties;
    @Autowired
    CompareBranchUtils compareBranchUtils;
    @Autowired
    MergeRequestUtils mergeRequestUtils;
    @Autowired
    ServiceMetaUtils serviceMetaUtils;

    @Qualifier("compareBranchUseDiff")
    @Autowired
    CompareBranchStrategy compareBranchStrategy;

    public void listenGitlab() {
        Set<Map.Entry<String, String>> assigneeMapEntries = assigneeProperties.getAssigneeMap().entrySet();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    for (Map.Entry<String, String> assigneeMapEntry : assigneeMapEntries) {
                        try {
                            // 1.遍历所有配置好的ServiceId
                            String serviceId = assigneeMapEntry.getKey();

                            // 2. 通过ServiceId去判断当前Service有没有提交信息，有的话则获取最新的提交信息
                            Meta newestMeta = serviceMetaUtils.getNewestMeta(serviceId);
                            if (newestMeta != null) {
                                String projectId = newestMeta.getProjectId();
                                String gitBranch = newestMeta.getGitBranch();

                                // 3. 检查当前提交的版本和目标版本（主干版本）是否不同
                                if (compareBranchUtils.compareBranchIsDiff(projectId, gitBranch, compareBranchStrategy)) {

                                    // 4. 如果两个版本不相同，则发送一个MR
                                    mergeRequestUtils.createMergeRequest(projectId, gitBranch, Integer.parseInt(assigneeMapEntry.getValue()));
                                }
                            }
                        } catch (IOException e) {
                            log.error("Http error!");
                            e.printStackTrace();
                        }
                    }
                }, 0, serviceProperties.getCheckTime(), TimeUnit.SECONDS
        );
    }
}
