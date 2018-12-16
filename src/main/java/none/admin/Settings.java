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

/**
 * @author peng-yongsheng
 */
public class Settings {

    private final JsonObject settings = new JsonObject();

    public Settings put(String key, Number value) {
        put(settings, key, value);
        return this;
    }

    private void put(JsonObject element, String key, Number value) {
        if (key.contains(".")) {
            String property = key.substring(0, key.indexOf("."));
            if (!element.has(property)) {
                element.add(property, new JsonObject());
            }
            put(element.getAsJsonObject(property), key.substring(key.indexOf(".") + 1), value);
        } else {
            element.addProperty(key, value);
        }
    }

    JsonObject build() {
        return settings;
    }
}
