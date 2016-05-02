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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for editing an object with a nested array of values.
 *
 * @author Carl Harris
 */
public class ArrayOfValuesTest extends ViewApplicatorTestBase {

  @Test
  public void testArrayModelPropertyAccess() throws Exception {
    validate(newTemplate(ArrayModel.class, AccessType.PROPERTY));
  }

  @Test
  public void testArrayModelFieldAccess() throws Exception {
    validate(newTemplate(ArrayModel.class, AccessType.FIELD));
  }

  @Test
  public void testCollectionModelPropertyAccess() throws Exception {
    validate(newTemplate(CollectionModel.class, AccessType.PROPERTY));
  }

  @Test
  public void testCollectionModelFieldAccess() throws Exception {
    validate(newTemplate(CollectionModel.class, AccessType.FIELD));
  }

  @Test
  public void testListModelPropertyAccess() throws Exception {
    validate(newTemplate(ListModel.class, AccessType.PROPERTY));
  }

  @Test
  public void testListModelFieldAccess() throws Exception {
    validate(newTemplate(ListModel.class, AccessType.FIELD));
  }

  protected ViewTemplate newTemplate(Class<?> modelType, AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("arrayOfValuesTest", modelType)
            .accessType(accessType)
            .arrayOfValues("values", "value", Object.class)
            .end()
        .build();
  }

  public static class ArrayModel {
    Object[] values;

    public Object[] getValues() {
      return values;
    }

    public void setValues(Object[] values) {
      this.values = values;
    }
  }

  public static class CollectionModel {
    Collection<Object> values = new ArrayList<>();

    public Collection<Object> getValues() {
      return values;
    }

    public void setValues(Collection<Object> values) {
      this.values = values;
    }

  }

  public static class ListModel {
    List<Object> values = new ArrayList<>();

    public List<Object> getValues() {
      return values;
    }

    public void setValues(List<Object> values) {
      this.values = values;
    }

  }


}
