package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.apache.commons.lang3.StringUtils;

/**
 * This Util class is used to sanitize the user input data from endpoint.
 */
public class RequestSanitizerUtil {

    public static String trimString(String requestValue) {

        return StringUtils.trim(requestValue);
    }

    public static boolean isNotEmpty(String requestValue) {

        return StringUtils.isNotEmpty(requestValue);
    }

    public static boolean isEmpty(String requestValue) {

        return StringUtils.isEmpty(requestValue);
    }
}
