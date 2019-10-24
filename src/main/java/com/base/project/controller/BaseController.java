package com.base.project.controller;

import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.project.common.enums.DownloadContentType;
import com.base.project.common.utils.CommonUtil;
import com.base.project.common.utils.RequestUtil;
import com.base.project.common.utils.ServletHolder;
import com.base.project.dto.UploadFileDto;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private HttpServletRequest getRequest() {
        return ServletHolder.getHttpServletRequest();
    }

    private HttpServletResponse getResponse() {
        return ServletHolder.getHttpServletResponse();
    }

    protected final Object getAgreeData(String key) {
        Map<String, Object> input = RequestUtil.getInput(getRequest());
        return input != null ? input.get(key) : null;
    }

    protected final String getDataJson() {
        Map<String, Object> input = RequestUtil.getInput(getRequest());
        return (String) input.get("data");
    }

    protected final String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    protected final Enumeration<String> getHeaders(String name) {
        return getRequest().getHeaders(name);
    }

    protected final Enumeration<String> getHeaderNames() {
        return getRequest().getHeaderNames();
    }

    protected final void setHeader(String name, String value) {
        getResponse().setHeader(name, value);
    }

    protected final void addDateHeader(String name, long date) {
        getResponse().addDateHeader(name, date);
    }

    protected final void setDateHeader(String name, long date) {
        getResponse().setDateHeader(name, date);
    }

    protected final long getDateHeader(String name) {
        return getRequest().getDateHeader(name);
    }

    protected final void setContentType(String type) {
        getResponse().setContentType(type);
    }

    protected final Cookie[] readCookies() {
        return getRequest().getCookies();
    }

    protected final void writeCookies(Cookie[] cookies) {
        HttpServletResponse response = getResponse();
        for (Cookie cookie : cookies)
            response.addCookie(cookie);
    }

    protected final void writeCookies(String domain, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookie.setDomain(domain);
        }
        writeCookies(cookies);
    }

    protected final void writeCookies(String domain, int expiry, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookie.setDomain(domain);
            cookie.setMaxAge(expiry);
        }
        writeCookies(cookies);
    }

    protected final void deleteCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
        }
        writeCookies(cookies);
    }

    protected final void setSessionCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(-1);
        }
        writeCookies(cookies);
    }

    protected final void writeHttpOnlyCookie(Cookie cookie) {
        StringBuilder builder = new StringBuilder();
        builder.append(cookie.getName() + "=" + cookie.getValue() + ";");
        if (cookie.getMaxAge() >= 0) {
            builder.append("Max-Age=" + cookie.getMaxAge() + ";");
        }
        builder.append("Domain=" + cookie.getDomain() + ";");
        builder.append("Path=" + cookie.getPath() + ";");
        builder.append("Version=" + cookie.getVersion() + ";");
        if (cookie.getSecure()) {
            builder.append("Secure;");
        }
        builder.append("HTTPOnly;");
        getResponse().addHeader("Set-Cookie", builder.toString());
    }

    protected final List<UploadFileDto> getUploadFileDto() {
        Map<String, Object> input = RequestUtil.getInput(getRequest());
        return (List) input.get("MULTIPART_FORMDATA");
    }

    protected final void download(byte[] bytes) throws IOException {
        download(bytes, CommonUtil.randomLetterOrDigit(10), DownloadContentType.DEFAULT.getValue());
    }

    protected final void download(byte[] bytes, String filename) throws IOException {
        download(bytes, filename, DownloadContentType.DEFAULT.getValue());
    }

    protected final void download(byte[] bytes, String filename, DownloadContentType contentType) throws IOException {
        download(bytes, filename, contentType.getValue());
    }

    protected final void download(byte[] bytes, String filename, String contentType) throws IOException {
        download(bytes, filename, contentType, false);
    }

    protected final void download(byte[] bytes, String filename, String contentType, boolean acao) throws IOException {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && ((userAgent.toLowerCase().indexOf("msie") > 0)
                || (userAgent.toLowerCase().indexOf("rv:11.0") > 0) || (userAgent.toLowerCase().indexOf("edge") > 0))) {
            filename = URLEncoder.encode(filename, "UTF-8");
            filename = filename.replaceAll("\\+", "%20");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO_8859_1");
        }
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-Length", "" + bytes.length);
        if (acao) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(bytes);
        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException localIOException) {
                }
        }
    }

    protected final void download(InputStream is, String filename, String contentType) throws IOException {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && ((userAgent.toLowerCase().indexOf("msie") > 0)
                || (userAgent.toLowerCase().indexOf("rv:11.0") > 0) || (userAgent.toLowerCase().indexOf("edge") > 0))) {
            filename = URLEncoder.encode(filename, "UTF-8");
            filename = filename.replaceAll("\\+", "%20");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO_8859_1");
        }
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");

        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        try {
            while (-1 != (n = is.read(buffer)))
                outputStream.write(buffer, 0, n);
        } finally {
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException localIOException) {
                }
        }
    }

    protected final void display(byte[] bytes, String filename, String contentType) throws IOException {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && ((userAgent.toLowerCase().indexOf("msie") > 0)
                || (userAgent.toLowerCase().indexOf("rv:11.0") > 0) || (userAgent.toLowerCase().indexOf("edge") > 0))) {
            filename = URLEncoder.encode(filename, "UTF-8");
            filename = filename.replaceAll("\\+", "%20");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO_8859_1");
        }

        response.setContentType(contentType);
        response.setHeader("Content-disposition", "inline; filename=" + filename);
        response.setHeader("Content-Length", "" + bytes.length);

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(bytes);
        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException localIOException) {
                }
        }
    }

    protected final void writeImage(RenderedImage im, String formatName) throws IOException {
        ImageIO.write(im, formatName, getResponse().getOutputStream());
    }

    protected final void write(String content) throws IOException {
        HttpServletResponse response = getResponse();
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.flush();
    }

    protected final void returnStatus(int code) {
        HttpServletResponse response = getResponse();
        response.setStatus(code);
    }

    protected final void return404() {
        HttpServletResponse response = getResponse();
        response.setStatus(404);
    }

    protected final void redirect301(String location) {
        redirect301(location, true);
    }

    protected final void redirect302(String location) {
        redirect302(location, true);
    }

    protected final void redirect301(String location, boolean needQuery) {
        redirect(301, location, needQuery);
    }

    protected final void redirect302(String location, boolean needQuery) {
        redirect(302, location, needQuery);
    }

    protected final void redirect(int status, String location, boolean needQuery) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        response.setHeader("Location", togetherUrlRequest(request, location, needQuery));
        response.setStatus(status);
    }

    protected final void redirectCrossDomain(int status, String url, boolean needQuery) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        String value = request.getQueryString();
        response.setHeader("Location", (needQuery) && (value != null) ? url + "?" + value : url);
        response.setStatus(status);
    }

    protected final String getPathInfo() {
        return getRequest().getPathInfo();
    }

    protected final String getContextPath() {
        return getRequest().getContextPath();
    }

    protected final String getRequestURI() {
        return getRequest().getRequestURI();
    }

    protected final String getServletPath() {
        return getRequest().getServletPath();
    }

    private String togetherUrlRequest(HttpServletRequest request, String location, boolean needQuery) {
        String value = request.getQueryString();
        return request.getScheme() + "://" + request.getHeader("host")
                + ((needQuery) && (value != null) ? location + "?" + value : location);
    }

    protected final void catUriMerge(String name) {
        getRequest().setAttribute("cat-page-uri", name);
    }

    protected final Enumeration<?> getParameterNames() {
        return getRequest().getParameterNames();
    }
}
