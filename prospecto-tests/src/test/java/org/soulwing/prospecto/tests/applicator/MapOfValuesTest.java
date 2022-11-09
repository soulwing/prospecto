/*
 * File created on Apr 4, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
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
package org.soulwing.prospecto.tests.applicator;

import java.util.Map;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for editing an object with a nested map of values.
 *
 * @author Carl Harris
 */
public class MapOfValuesTest extends ViewApplicatorTestBase {

  @Test
  public void testModelPropertyAccess() throws Exception {
    validate(newTemplate(MapModel.class, AccessType.PROPERTY));
  }

  @Test
  public void testModelFieldAccess() throws Exception {
    validate(newTemplate(MapModel.class, AccessType.FIELD));
  }

  protected ViewTemplate newTemplate(Class<?> modelType, AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("mapOfValuesTest", modelType)
            .accessType(accessType)
            .mapOfValues("values", Object.class, Object.class)
            .end()
        .build();
  }

  public static class MapModel {
    Map<Object, Object> values;

    public Map<Object, Object> getValues() {
      return values;
    }

    public void setValues(Map<Object, Object> values) {
      this.values = values;
    }
  }

}
