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

package none.admin;

import com.google.gson.JsonObject;
import org.junit.*;

/**
 * @author peng-yongsheng
 */
public class SettingsTestCase {

    @Test
    public void testPutNumber() {
        Settings settings = new Settings();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 2);

        JsonObject result = settings.build();
        Assert.assertEquals(3, result.get("number_of_shards").getAsInt());
        Assert.assertEquals(2, result.get("number_of_replicas").getAsInt());

        settings = new Settings();
        settings.put("index.number_of_shards", 3);
        settings.put("index.number_of_replicas", 2);

        result = settings.build();

        Assert.assertEquals(3, result.getAsJsonObject("index").get("number_of_shards").getAsInt());
        Assert.assertEquals(2, result.getAsJsonObject("index").get("number_of_replicas").getAsInt());

        settings = new Settings();
        settings.put("index.test.number_of_shards", 3);
        settings.put("index.test.number_of_replicas", 2);

        result = settings.build();

        Assert.assertEquals(3, result.getAsJsonObject("index").getAsJsonObject("test").get("number_of_shards").getAsInt());
        Assert.assertEquals(2, result.getAsJsonObject("index").getAsJsonObject("test").get("number_of_replicas").getAsInt());
    }
}
