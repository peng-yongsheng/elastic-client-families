package none.admin;

import com.google.gson.*;
import java.io.IOException;
import java.util.Objects;
import none.ElasticClient;
import okhttp3.*;
import org.apache.logging.log4j.*;

/**
 * @author peng-yongsheng
 */
public class AdminClient {

    private static final Logger logger = LogManager.getLogger(AdminClient.class);

    private final ElasticClient client;

    public AdminClient(ElasticClient client) {
        this.client = client;
    }

    public IndexCreateResponse create(String indexName, Settings settings, Mappings mappings) throws IOException {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder().scheme("http");
        httpUrl.host(client.activeNode().getHost());
        httpUrl.port(client.activeNode().getPort());
        httpUrl.addPathSegment(indexName);

        JsonObject content = new JsonObject();
        content.add("settings", settings.build());

        RequestBody requestBody = RequestBody.create(ElasticClient.JSON, content.toString());

        Request request = new Request.Builder()
            .url(httpUrl.build())
            .method("PUT", requestBody)
            .build();

        Response response = client.call(request);

        IndexCreateResponse indexCreateResponse = new IndexCreateResponse(response.code(), response.message());
        if (response.code() == 200 && Objects.nonNull(response.body())) {
            Gson gson = new Gson();

            JsonObject body = gson.fromJson(response.body().charStream(), JsonObject.class);
            indexCreateResponse.setAcknowledged(body.get("acknowledged").getAsBoolean());
            indexCreateResponse.setShardsAcknowledged(body.get("shards_acknowledged").getAsBoolean());
            indexCreateResponse.setIndex(body.get("index").getAsString());

            if (logger.isDebugEnabled()) {
                logger.debug("Status Code: {}, message: {}, body: {}", response.code(), response.message(), body);
            }
        }

        return indexCreateResponse;
    }

    public void get(String indexName) throws IOException {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder().scheme("http");
        httpUrl.host(client.activeNode().getHost());
        httpUrl.port(client.activeNode().getPort());
        httpUrl.addEncodedPathSegment(indexName);

        Request request = new Request.Builder()
            .url(httpUrl.build())
            .get()
            .build();

        client.call(request);
    }

    public boolean delete(String indexName) throws IOException {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder().scheme("http");
        httpUrl.host(client.activeNode().getHost());
        httpUrl.port(client.activeNode().getPort());
        httpUrl.addEncodedPathSegment(indexName);

        Request request = new Request.Builder()
            .url(httpUrl.build())
            .delete()
            .build();

        Response response = client.call(request);
        if (response.code() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public boolean exists(String indexName) throws IOException {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder().scheme("http");
        httpUrl.host(client.activeNode().getHost());
        httpUrl.port(client.activeNode().getPort());
        httpUrl.addEncodedPathSegment(indexName);

        Request request = new Request.Builder()
            .url(httpUrl.build())
            .head()
            .build();

        Response response = client.call(request);
        if (response.code() == 200) {
            return true;
        } else {
            return false;
        }
    }
}
