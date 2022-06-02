package com.mindlinker.listengitlab.untis.ServiceMetaUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindlinker.listengitlab.model.Meta;

import java.io.UnsupportedEncodingException;

public interface ServiceMetaUtils {

    Meta getNewestMeta(String serviceId) throws JsonProcessingException, UnsupportedEncodingException;
}
