package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.apache.commons.lang3.StringUtils;

public class RequestSnatizerUtil {

    public static String trimString(String requestValue) {

        return StringUtils.trim(requestValue);
    }

    public static boolean isNotEmpty(String requestValue){

        return StringUtils.isNotEmpty(requestValue);
    }

    public static boolean isEmpty(String requestValue){

        return StringUtils.isEmpty(requestValue);
    }
}
