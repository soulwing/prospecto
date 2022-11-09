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

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for an object with multiple subtypes.
 * @author Carl Harris
 */
public class MapOfObjectsSubtypeTest extends ObjectSubtypeTest {

  @Test
  public void testWithPropertyAccess() throws Exception {
    validate(newTemplate(AccessType.PROPERTY));
  }

  @Test
  public void testWithFieldAccess() throws Exception {
    validate(newTemplate(AccessType.FIELD));
  }


  protected ViewTemplate newTemplate(AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("mapOfObjectsSubtypeTest", ParentModel.class)
            .accessType(accessType)
            .mapOfObjects("vehicles", String.class, newVehicleTemplate(accessType))
            .end()
        .build();
  }

  @SuppressWarnings("unused")
  public static class ParentModel {
    private Map<String, Vehicle> vehicles = new LinkedHashMap<>();

    public Map<String, Vehicle> getVehicles() {
      return vehicles;
    }

    public void setVehicles(Map<String, Vehicle> vehicles) {
      this.vehicles = vehicles;
    }
  }


}
