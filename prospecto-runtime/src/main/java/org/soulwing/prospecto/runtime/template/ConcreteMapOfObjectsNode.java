/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.template;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;
import org.soulwing.prospecto.api.template.MapOfObjectsNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteMapAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MapAccessor;
import org.soulwing.prospecto.runtime.accessor.MapAccessorFactory;

/**
 * A view node that represents an array of objects.
 *
 * @author Carl Harris
 */
public class ConcreteMapOfObjectsNode extends ConcreteContainerNode
    implements MapOfObjectsNode {

  private final MapAccessorFactory accessorFactory;

  private MapAccessor mapAccessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param modelType model type of the array elements
   */
  public ConcreteMapOfObjectsNode(String name,
      String namespace, Class<?> modelType) {
    this(name, namespace, modelType,
        ConcreteMapAccessorFactory.INSTANCE
    );
  }

  ConcreteMapOfObjectsNode(String name,
      String namespace, Class<?> modelType,
      MapAccessorFactory accessorFactory) {
    super(name, namespace, modelType);
    this.accessorFactory = accessorFactory;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.mapAccessor = accessorFactory.newAccessor(accessor);
  }

  @Override
  public String getPropertyName() {
    return getAccessor().getName();
  }

  @Override
  public Class<?> getComponentType() {
    return Object.class;
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return mapAccessor != null ?
        mapAccessor.getSupportedModes() : EnumSet.allOf(AccessMode.class);
  }

  @Override
  public ToManyMappedAssociationManager<?, ?, ?> getDefaultManager() {
    return mapAccessor;
  }

  @Override
  public Iterator<Map.Entry> iterator(Object model) throws Exception {
    return getModelIterator(model);
  }

  protected Iterator<Map.Entry> getModelIterator(Object source)
      throws Exception {
    return mapAccessor.iterator(source);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitMapOfObjects(this, state);
  }

}
