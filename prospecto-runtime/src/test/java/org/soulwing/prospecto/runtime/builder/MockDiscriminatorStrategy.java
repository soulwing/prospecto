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

import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;

/**
 * A mock {@link DiscriminatorStrategy} used in testing the template builder.
 *
 * @author Carl Harris
 */
public class MockDiscriminatorStrategy implements DiscriminatorStrategy {

  private String mockProperty;

  /**
   * Gets the {@code mockProperty} property.
   * @return property value
   */
  public String getMockProperty() {
    return mockProperty;
  }

  /**
   * Sets the {@code mockProperty} property.
   * @param mockProperty the property value to set
   */
  public void setMockProperty(String mockProperty) {
    this.mockProperty = mockProperty;
  }

  @Override
  public Discriminator toDiscriminator(Class<?> base, Class<?> subtype) {
    return null;
  }

  @Override
  public <T> Class<T> toSubtype(Class<T> base, Discriminator discriminator)
      throws ClassNotFoundException {
    return null;
  }
}
