package com.elcom.its.report.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ducduongn
 */
public class Utils {
    public static String getParams(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd)
            throws UnsupportedEncodingException {
        String params = null;
        params = "filterObjectType=" + URLEncoder.encode(filterObjectType, "UTF-8")
                + "&filterTimeLevel=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                + "&filterObjectIds=" + URLEncoder.encode(filterObjectIds, "UTF-8")
                + "&isAdminBackEnd=" + isAdminBackEnd;
        return params;
    }
}
