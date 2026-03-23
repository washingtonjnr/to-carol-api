package com.tocarol.api.adapter.out.storage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.tocarol.api.adapter.out.http.HttpGateway;
import com.tocarol.api.domain.model.Image;
import com.tocarol.api.domain.port.out.ImageStoragePort;

@Component
@ConditionalOnProperty(name = "storage.provider", havingValue = "supabase", matchIfMissing = true)
public class SupabaseImageStorageGateway implements ImageStoragePort {

    private final HttpGateway httpGateway;
    private final String supabaseUrl;
    private final String supabaseKey;
    private final String bucket;

    public SupabaseImageStorageGateway(
            HttpGateway httpGateway,
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey,
            @Value("${supabase.storage.bucket}") String bucket) {
        this.httpGateway = httpGateway;
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.bucket = bucket;
    }
    @Override
    public List<Image> listAll() {
        String url = supabaseUrl + "/storage/v1/object/list/" + bucket;
        Map<String, String> headers = Map.of("Authorization", "Bearer " + supabaseKey);
        Map<String, Object> body = Map.of("prefix", "", "limit", 100, "offset", 0);

        SupabaseFileObject[] files = httpGateway.post(url, headers, body, SupabaseFileObject[].class);

        if (files == null) {
            return List.of();
        }


        return Arrays.stream(files)
                .map(f -> new Image(
                        f.name(),
                        supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + f.name()))
                .toList();
    }
}
