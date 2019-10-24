package com.base.project.common.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static Map<String, Object> getInput(HttpServletRequest request) {
        return (Map) request.getAttribute("input");
    }

    public static Object getOutput(HttpServletRequest request) {
        return request.getAttribute("output");
    }

    public static void setInput(HttpServletRequest request, Map<String, Object> input) {
        request.setAttribute("input", input);
    }

    public static void setOutput(HttpServletRequest request, Object output) {
        request.setAttribute("output", output);
    }

    public static void clean(HttpServletRequest request) {
        request.removeAttribute("input");
        request.removeAttribute("output");
        AttributeUtil.clean(request);
    }

    public static Map<String, Object> getParameters(HttpServletRequest request) {
        Set<Entry<String, String[]>> set = request.getParameterMap().entrySet();
        Map<String, Object> inputMap = new LinkedHashMap<String, Object>();
        for (Map.Entry entry : set) {
            String key = (String) entry.getKey();
            String[] value = (String[]) entry.getValue();
            if ((value != null) && (value.length != 0)) {
                if (value.length == 1)
                    inputMap.put(key, value[0]);
                else {
                    inputMap.put(key, Arrays.asList(value));
                }
            }
        }
        return inputMap;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                || (request.getParameter("callback") != null);
    }

    public static boolean isJsonRequest(HttpServletRequest request) {
        return request.getRequestURI().endsWith(".json");
    }

    public static boolean isAppRequest(HttpServletRequest request) {
        return (isJsonRequest(request)) && (!isAjaxRequest(request));
    }

    public static boolean isGzipRequest(HttpServletRequest request) {
        String header = request.getHeader("accept-encoding");

        return (header != null) && (header.toLowerCase().indexOf("gzip") != -1);
    }

    public static boolean isWxaRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Client-Type");
        return (header != null) && (header.toLowerCase().equals("wxa"));
    }

    public static boolean needGzipHandle(HttpServletRequest request, boolean isRPC) {
        return (isGzipRequest(request)) && ((isRPC)
                || ((!isRPC) && (isJsonRequest(request)) && (!isAjaxRequest(request)) && (!isWxaRequest(request))));
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-lietou-Client-IP");
        if ((ip == null) || (ip.trim().length() == 0)) {
            return request.getRemoteAddr();
        }
        return ip;
    }
}