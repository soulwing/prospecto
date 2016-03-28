/*
 * File created on Mar 28, 2016
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
package org.soulwing.prospecto.runtime.builder;

import java.util.Collection;

/**
 * A mock model used for testing the template builder.
 *
 * @author Carl Harris
 */
public class MockModel {

  private MockChildModel mockField;

  private MockChildModel otherMockField;

  public MockChildModel getMockProperty() {
    return null;
  }

  public void setMockProperty(MockChildModel mockProperty) {
  }

  public MockChildModel getOtherMockProperty() {
    return null;
  }

  public void setOtherMockProperty(MockChildModel otherMockProperty) {
  }

  public Object[] getMockArray() {
    return null;
  }

  public void setMockArray(Object[] mockArray) {
  }

  public Collection getMockCollection() {
    return null;
  }

  public void setMockArray(Collection mockArray) {
  }


}
