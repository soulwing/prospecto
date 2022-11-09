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
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.MapOfValuesNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteKeyValueAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.KeyValueAccessor;
import org.soulwing.prospecto.runtime.accessor.KeyValueAccessorFactory;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents a map of values.
 *
 * @author Carl Harris
 */
public class ConcreteMapOfValuesNode extends AbstractViewNode
    implements Convertible, MapOfValuesNode {

  private final Class<?> keyType;
  private final Class<?> componentType;
  private final KeyValueAccessorFactory accessorFactory;

  private KeyValueAccessor keyValueAccessor;

  /**
   * Constructs a new instance.
   *
   * @param name node name
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param keyType common type for keys
   * @param componentType common type for elements
   */
  public ConcreteMapOfValuesNode(String name,
      String namespace, Class<?> keyType, Class<?> componentType) {
    this(name, namespace, keyType, componentType,
        ConcreteKeyValueAccessorFactory.INSTANCE
    );
  }

  ConcreteMapOfValuesNode(String name, String namespace,
      Class<?> keyType, Class<?> componentType,
      KeyValueAccessorFactory accessorFactory) {
    super(name, namespace, null, null);
    this.keyType = keyType;
    this.componentType = componentType;
    this.accessorFactory = accessorFactory;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.keyValueAccessor = accessor != null ?
        accessorFactory.newAccessor(accessor, keyType, componentType) : null;
  }

  @Override
  public String getPropertyName() {
    return getAccessor().getName();
  }

  @Override
  public Class<?> getKeyType() {
    return keyType;
  }

  @Override
  public Class<?> getComponentType() {
    return keyValueAccessor.getComponentType();
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return keyValueAccessor != null ?
        keyValueAccessor.getSupportedModes() : EnumSet.allOf(AccessMode.class);
  }

  @Override
  public ToManyAssociationManager<?, ?> getDefaultManager() {
    return keyValueAccessor;
  }

  @Override
  public Iterator<Map.Entry<?, ?>> iterator(Object model) throws Exception {
    final Iterator<Map.Entry<Object, Object>> iterator = getModelIterator(model);
    return new Iterator<Map.Entry<?, ?>>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public Map.Entry<?, ?> next() {
        return iterator.next();
      }
    };
  }

  protected Iterator<Map.Entry<Object, Object>> getModelIterator(Object source) throws Exception {
    return keyValueAccessor.iterator(source);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitMapOfValues(this, state);
  }

}
