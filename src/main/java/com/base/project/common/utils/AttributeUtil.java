package com.base.project.common.utils;

import javax.servlet.http.HttpServletRequest;

public class AttributeUtil {
    public static final String ATTRIBUTE_REQUEST_URI = "javax.servlet.request_uri";
    public static final String ATTRIBUTE_ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ATTRIBUTE_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    public static final String ATTRIBUTE_JSONFORM_DATA = "javax.servlet.jsonform.data";

    public static final void setErrorStatusCode(HttpServletRequest request, int code) {
        request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(code));
    }

    public static final Integer getErrorStatusCode(HttpServletRequest request) {
        return (Integer) request.getAttribute("javax.servlet.error.status_code");
    }

    public static final void setJsonFormData(HttpServletRequest request, Object data) {
        request.setAttribute("javax.servlet.jsonform.data", data);
    }

    public static final Object getJsonFormData(HttpServletRequest request) {
        return request.getAttribute("javax.servlet.jsonform.data");
    }

    public static final void clean(HttpServletRequest request) {
        request.removeAttribute("javax.servlet.error.status_code");
        request.removeAttribute("javax.servlet.jsonform.data");
    }
}