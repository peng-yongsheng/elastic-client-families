/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package none;

import java.io.IOException;
import none.admin.AdminClient;
import okhttp3.*;
import org.apache.logging.log4j.*;

/**
 * @author peng-yongsheng
 */
public class ElasticClient {

    private static final Logger logger = LogManager.getLogger(ElasticClient.class);

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final AdminClient adminClient;
    private final HostPort[] hostPorts;

    public ElasticClient() {
        this(new Builder());
    }

    private ElasticClient(Builder builder) {
        this.client = new OkHttpClient.Builder().build();
        this.hostPorts = builder.hostPorts;
        this.adminClient = new AdminClient(this);
    }

    public HostPort activeNode() {
        return hostPorts[0];
    }

    public AdminClient admin() {
        return adminClient;
    }

    public Response call(Request request) throws IOException {
        Response response = client.newCall(request).execute();

        if (logger.isDebugEnabled()) {
            logger.debug("Status Code: {}, message: {}", response.code(), response.message());
        }

        return response;
    }

    public static final class Builder {

        private final HostPort[] hostPorts;

        public Builder(HostPort... hostPorts) {
            this.hostPorts = hostPorts;
        }

        public ElasticClient build() {
            return new ElasticClient(this);
        }
    }
}
