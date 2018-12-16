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
import none.admin.*;
import org.junit.*;

/**
 * @author peng-yongsheng
 */
public class ITAdminOperateTestCase {

    @Test
    public void create() throws IOException {
        Settings settings = new Settings();
        settings.put("index.number_of_shards", 3);
        settings.put("index.number_of_replicas", 0);

        ElasticClient client = new ElasticClient.Builder(new HostPort("127.0.0.1", 9200)).build();
        IndexCreateResponse indexCreateResponse = client.admin().create("admin_test_index", settings, new Mappings());
        Assert.assertEquals(200, indexCreateResponse.getCode());
        Assert.assertEquals("OK", indexCreateResponse.getMessage());
        Assert.assertTrue(indexCreateResponse.isAcknowledged());
        Assert.assertTrue(indexCreateResponse.isShardsAcknowledged());
        Assert.assertEquals("admin_test_index", indexCreateResponse.getIndex());

        client.admin().get("admin_test_index");

        boolean exists = client.admin().exists("admin_test_index");
        Assert.assertTrue(exists);

        boolean delete = client.admin().delete("admin_test_index");
        Assert.assertTrue(delete);

        exists = client.admin().exists("admin_test_index");
        Assert.assertFalse(exists);
    }
}
