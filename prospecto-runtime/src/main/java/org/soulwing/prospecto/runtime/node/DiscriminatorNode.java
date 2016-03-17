/*
 * File created on Mar 17, 2016
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
package org.soulwing.prospecto.runtime.node;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A value node that represents a subtype discriminator.
 *
 * @author Carl Harris
 */
public class DiscriminatorNode extends ValueViewNode {

  private Class<?> base;

  public DiscriminatorNode() {
    super(null, null);
  }

  /**
   * Gets the {@code base} property.
   * @return property value
   */
  public Class<?> getBase() {
    return base;
  }

  /**
   * Sets the {@code base} property.
   * @param base the property value to set
   */
  public void setBase(Class<?> base) {
    this.base = base;
  }

  @Override
  protected Object getModelValue(Object source, ScopedViewContext context)
      throws Exception {
    final DiscriminatorStrategy strategy = get(DiscriminatorStrategy.class);
    assert strategy != null;
    return strategy.toDiscriminator(getBase(), source.getClass());
  }

  @Override
  protected View.Event.Type getEventType() {
    return View.Event.Type.DISCRIMINATOR;
  }

  @Override
  protected String getEventName(Object model, ScopedViewContext context) {
    return ((Discriminator) model).getName();
  }

  @Override
  protected Object toViewValue(Object model, ScopedViewContext context)
      throws Exception {
    return ((Discriminator) model).getValue();
  }


}
