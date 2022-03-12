package stream.support.command.api.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.io.EmptyInputStream;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class GetRequest<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String url;
    private final Class<T> clazz;

    public GetRequest(String url, Class<T> clazz) {
        this.url = url;
        this.clazz = clazz;
    }

    public Optional<T> execute() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", "latest-cotd-player-position by Tag365 (Tag365#0365)");
            HttpResponse response = client.execute(request);
            log.info("Request send to {}", url);
            if (response.getStatusLine().getStatusCode() == 200 && !(response.getEntity().getContent() instanceof EmptyInputStream)) {
                T result = mapper.readValue(response.getEntity().getContent(), clazz);
                return Optional.of(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
