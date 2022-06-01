package com.mindlinker.listengitlab.untis.ServiceMetaUtils.Impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindlinker.listengitlab.model.Meta;
import com.mindlinker.listengitlab.properties.GitlabProperties;
import com.mindlinker.listengitlab.properties.ServiceProperties;
import com.mindlinker.listengitlab.untis.HttpUtils;
import com.mindlinker.listengitlab.untis.ServiceMetaUtils.ServiceMetaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class PraseGitlabServiceMetaUtils implements ServiceMetaUtils {

    @Autowired
    ServiceProperties serviceProperties;
    @Autowired
    GitlabProperties gitlabProperties;

    @Override
    public Meta getNewestMeta(String serviceId) throws IOException {
        log.debug(this.getClass().getName() + ".getNewestMeta(): method begin, and serviceId = " + serviceId);

        String serviceInfoAgreement = serviceProperties.getAgreement();
        String serviceInfoAddress = serviceProperties.getAddress();
        String serviceInfoPort = serviceProperties.getPort();
        String serviceInfoPrefix = serviceProperties.getPrefix();
        String serviceInfoSuffix = serviceProperties.getSuffix();
        String url = serviceInfoAgreement + "://" + serviceInfoAddress + ":" + serviceInfoPort + serviceInfoPrefix + serviceId + serviceInfoSuffix;
        List<Meta> metaList = new ArrayList<>();

        log.debug(this.getClass().getName() + ".getNewestMeta(): Ready to visit Consual");
        String resp = HttpUtils.sendGetRequest(url, null);
        log.debug(this.getClass().getName() + ".getNewestMeta(): resp = " + resp);

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

        log.debug(this.getClass().getName() + ".getNewestMeta(): metaList.size() = " + metaList.size());
        if (metaList.size() > 0) {
            Collections.sort(metaList, new Comparator<Meta>() {
                @Override
                public int compare(Meta meta1, Meta meta2) {
                    return (int) (meta2.getRegisterTimestamp() - meta1.getRegisterTimestamp());
                }
            });
            Meta newestMeta = metaList.get(0);
            log.debug("return newestMeta");
            return newestMeta;
        } else {
            log.debug("newestMeta is null");
            return null;
        }
    }
}
