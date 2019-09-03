/*
 * File created on Aug 28, 2019
 *
 * Copyright (c) 2019 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.prospecto.api.json;

import javax.json.JsonStructure;

import org.soulwing.prospecto.api.ViewWriter;

/**
 * A target for a {@link ViewWriter} that holds a JSON-P structure.
 *
 * @author Carl Harris
 */
public class JsonPTarget implements ViewWriter.Target {

  private JsonStructure json;

  public void store(JsonStructure json) {
    this.json = json;
  }

  public JsonStructure toJson() {
    return json;
  }

}
