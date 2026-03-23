package com.tocarol.api.adapter.out.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RestClientHttpGateway implements HttpGateway {

    private final RestClient restClient;

    public RestClientHttpGateway() {
        this.restClient = RestClient.create();
    }

    @Override
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        return restClient.get()
                .uri(url)
                .headers(h -> headers.forEach(h::set))
                .retrieve()
                .body(responseType);
    }

    @Override
    public <T> T post(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        return restClient.post()
                .uri(url)
                .headers(h -> headers.forEach(h::set))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    @Override
    public <T> T put(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        return restClient.put()
                .uri(url)
                .headers(h -> headers.forEach(h::set))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    @Override
    public <T> T patch(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        return restClient.patch()
                .uri(url)
                .headers(h -> headers.forEach(h::set))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    @Override
    public void delete(String url, Map<String, String> headers) {
        restClient.delete()
                .uri(url)
                .headers(h -> headers.forEach(h::set))
                .retrieve()
                .toBodilessEntity();
    }
}
