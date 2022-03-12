package stream.support.command.api.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.io.EmptyInputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetRequest<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String url;
    private final Class<T> clazz;
    private final Map<String, String> headers;

    private byte[] rawResponse;
    private T parsedResponse;

    GetRequest(String url, Class<T> clazz, Map<String, String> headers) {
        this.url = url;
        this.clazz = clazz;
        this.headers = headers;
    }

    public GetRequest<T> execute() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            headers.forEach(request::addHeader);
            HttpResponse response = client.execute(request);
            log.info("Request send to {}", url);
            if (response.getStatusLine().getStatusCode() == 200 && !(response.getEntity().getContent() instanceof EmptyInputStream)) {
                rawResponse = response.getEntity().getContent().readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @SneakyThrows
    public GetRequest<T> parse() {
        parsedResponse = mapper.readValue(rawResponse, clazz);
        return this;
    }

    public Optional<T> getParsedResponse() {
        return Optional.ofNullable(this.parsedResponse);
    }

    @SneakyThrows
    public String getRawResponse() {
        return new String(rawResponse, StandardCharsets.UTF_8);
    }
}


