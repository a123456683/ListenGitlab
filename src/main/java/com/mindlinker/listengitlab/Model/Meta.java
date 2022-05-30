/**
 * Copyright 2022 json.cn
 */
package com.mindlinker.listengitlab.Model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
public class Meta {

    private String gitCommitId;
    private String gitRemoteOriginUrl;
    private Long registerTimestamp;
    private String projectId;
    private String gitBranch;

    public void parseProjectId(String gitlabAddress) throws UnsupportedEncodingException {
        String[]  strs = this.gitRemoteOriginUrl.split(gitlabAddress);
        String projectId = StringUtils.substringBefore(strs[1], ".git");
        projectId = StringUtils.substringAfter(projectId, "/");
        this.projectId = URLEncoder.encode(projectId, "UTF-8").replace("+", "%20");
    }

    public boolean checkMetaIsCommitted() {
        if (this.gitCommitId == null || this.gitRemoteOriginUrl == null || this.registerTimestamp == null) {
            return false;
        } else {
            return true;
        }
    }

}