package com.mindlinker.listengitlab.untis.ServiceMetaUtils;

import com.mindlinker.listengitlab.model.Meta;

import java.io.IOException;

public interface ServiceMetaUtils {

    Meta getNewestMeta(String serviceId) throws IOException;
}
