package com.base.project.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.project.common.utils.ServletHolder.Holder;

public class ServletHolder extends ThreadLocal<Holder> {
    private static final ServletHolder instance = new ServletHolder();

    protected Holder initialValue() {
        return new Holder();
    }

    public static void assign(HttpServletRequest request, HttpServletResponse response) {
        ((Holder) instance.get()).assign(request, response);
    }

    public static void reset() {
        ((Holder) instance.get()).reset();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((Holder) instance.get()).getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return ((Holder) instance.get()).getResponse();
    }

    public static class Holder {
        private HttpServletRequest request;
        private HttpServletResponse response;

        public void reset() {
            this.request = null;
            this.response = null;
        }

        public void assign(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

        public HttpServletRequest getRequest() {
            return this.request;
        }

        public HttpServletResponse getResponse() {
            return this.response;
        }
    }
}