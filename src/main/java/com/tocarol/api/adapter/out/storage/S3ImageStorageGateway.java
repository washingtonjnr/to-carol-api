package com.tocarol.api.adapter.out.storage;

import com.tocarol.api.adapter.out.http.HttpGateway;
import com.tocarol.api.domain.model.Image;
import com.tocarol.api.domain.port.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "storage.provider", havingValue = "s3")
public class S3ImageStorageGateway implements ImageStoragePort {

    private final HttpGateway httpGateway;
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucket;
    private final String region;

    public S3ImageStorageGateway(
            HttpGateway httpGateway,
            @Value("${s3.endpoint}") String endpoint,
            @Value("${s3.access-key}") String accessKey,
            @Value("${s3.secret-key}") String secretKey,
            @Value("${s3.bucket}") String bucket,
            @Value("${s3.region}") String region) {
        this.httpGateway = httpGateway;
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.region = region;
    }

    @Override
    public List<Image> listAll() {
        String url = endpoint + "/" + bucket + "?list-type=2&max-keys=1000";
        // AWS Signature V4 headers must be computed and passed here.
        // accessKey and secretKey are available for signing.
        Map<String, String> headers = Map.of("x-amz-content-sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");

        String xml = httpGateway.get(url, headers, String.class);

        return parseKeys(xml).stream()
                .map(key -> new Image(key, endpoint + "/" + bucket + "/" + key))
                .toList();
    }

    private List<String> parseKeys(String xml) {
        List<String> keys = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            NodeList nodes = doc.getElementsByTagName("Key");
            for (int i = 0; i < nodes.getLength(); i++) {
                keys.add(nodes.item(i).getTextContent());
            }
        } catch (Exception ignored) {
        }
        return keys;
    }
}
