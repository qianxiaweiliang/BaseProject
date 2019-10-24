package com.base.project.dto;

import java.util.HashMap;
import java.util.Map;

public class UploadFileDto {
    private String name;
    private String fieldName;
    private byte[] bytes;
    private String contentType;
    private Map<String, String> metadata = new HashMap<String, String>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void appendMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }
}