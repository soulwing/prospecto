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

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for all of the basic data types allowed, nested in an object.
 *
 * @author Carl Harris
 */
public class NestedValueTest extends ValueTest {

  @Test
  public void testWrapperTypesPropertyAccess() throws Exception {
    validate(newTemplate(WrapperTypesParent.class, WrapperTypes.class,
        AccessType.PROPERTY));
  }

  @Test
  public void testPrimitiveTypesPropertyAccess() throws Exception {
    validate(newTemplate(PrimitiveTypesParent.class, PrimitiveTypes.class,
        AccessType.PROPERTY));
  }

  @Test
  public void testWrapperTypesFieldAccess() throws Exception {
    validate(newTemplate(WrapperTypesParent.class, WrapperTypes.class,
        AccessType.FIELD));
  }

  @Test
  public void testPrimitiveTypesFieldAccess() throws Exception {
    validate(newTemplate(PrimitiveTypesParent.class, PrimitiveTypes.class,
        AccessType.FIELD));
  }

  private ViewTemplate newTemplate(Class<?> parentType, Class<?> childType,
      AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("nestedValueTest", parentType)
            .accessType(accessType)
            .object("child", super.newTemplate(childType, accessType))
            .end()
        .build();
  }

  @SuppressWarnings("unused")
  public static class PrimitiveTypesParent {

    private PrimitiveTypes child;

    public PrimitiveTypes getChild() {
      return child;
    }

    public void setChild(PrimitiveTypes child) {
      this.child = child;
    }
  }

  @SuppressWarnings("unused")
  public static class WrapperTypesParent {

    private WrapperTypes child;

    public WrapperTypes getChild() {
      return child;
    }

    public void setChild(WrapperTypes child) {
      this.child = child;
    }

  }

}