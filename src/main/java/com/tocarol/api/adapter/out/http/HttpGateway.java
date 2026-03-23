package com.tocarol.api.adapter.out.http;

import java.util.Map;

public interface HttpGateway {
    <T> T get(String url, Map<String, String> headers, Class<T> responseType);
    <T> T post(String url, Map<String, String> headers, Object body, Class<T> responseType);
    <T> T put(String url, Map<String, String> headers, Object body, Class<T> responseType);
    <T> T patch(String url, Map<String, String> headers, Object body, Class<T> responseType);
    void delete(String url, Map<String, String> headers);
}
