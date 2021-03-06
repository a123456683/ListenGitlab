package com.mindlinker.listengitlab.listener;

import com.mindlinker.listengitlab.model.Meta;
import com.mindlinker.listengitlab.properties.AssigneeProperties;
import com.mindlinker.listengitlab.properties.ServiceProperties;
import com.mindlinker.listengitlab.strategy.CompareBranchStrategy;
import com.mindlinker.listengitlab.untis.BranchUtils.BranchUtils;
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
    @Autowired
    BranchUtils branchUtils;

    @Qualifier("compareBranchUseDiff")
    @Autowired
    CompareBranchStrategy compareBranchStrategy;

    public void listenGitlab() {
        Set<Map.Entry<String, String>> assigneeMapEntries = assigneeProperties.getAssigneeMap().entrySet();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    String projectId = null;
                    try {
                        for (Map.Entry<String, String> assigneeMapEntry : assigneeMapEntries) {
                            // 1.????????????????????????ServiceId
                            String serviceId = assigneeMapEntry.getKey();

                            // 2. ??????ServiceId???????????????Service???????????????????????????????????????????????????????????????
                            Meta newestMeta = serviceMetaUtils.getNewestMeta(serviceId);
                            if (newestMeta != null) {
                                projectId = newestMeta.getProjectId();
                                String gitBranch = newestMeta.getGitBranch();
                                String gitCommitId = newestMeta.getGitCommitId();

                                // 3. ????????????????????????????????????????????????????????????????????????
                                if (compareBranchUtils.compareBranchIsDiff(projectId, gitBranch, compareBranchStrategy)) {

                                    // 4. ?????????????????????????????????????????????MR
                                    // 4.1 ????????????????????????
                                    String repositoryBranchName = gitBranch + "_repositoryBranch_" + gitCommitId;
                                    boolean isCreateRepositoryBranch = branchUtils.createRepositoryBranch(projectId, repositoryBranchName, gitCommitId);

                                    // 4.2 ??????????????????????????????MR
                                    String result = mergeRequestUtils.createMergeRequest(projectId, repositoryBranchName, Integer.parseInt(assigneeMapEntry.getValue()));
                                    // 4.3 ??????????????????
                                    if (isCreateRepositoryBranch && !mergeRequestUtils.judgeCreateMRsuccessfully(result)){
                                        branchUtils.deleteRepositoryBranch(projectId, repositoryBranchName);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("Http error!");
                        e.printStackTrace();
                    } finally {
                        branchUtils.deleteAllMergedBranch(projectId);
                    }
                }, 0, serviceProperties.getCheckTime(), TimeUnit.SECONDS
        );
    }
}
