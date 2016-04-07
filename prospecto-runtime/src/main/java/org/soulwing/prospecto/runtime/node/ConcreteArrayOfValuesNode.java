/*
 * File created on Mar 9, 2016
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

import java.util.Iterator;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.node.ArrayOfValuesNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteMultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents an array of values.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfValuesNode extends AbstractViewNode
    implements Convertible, ArrayOfValuesNode {

  private final String elementName;
  private final Class<?> componentType;
  private final MultiValuedAccessorFactory accessorFactory;

  private MultiValuedAccessor multiValuedAccessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param componentType common type for elements
   */
  public ConcreteArrayOfValuesNode(String name, String elementName,
      String namespace, Class<?> componentType) {
    this(name, elementName, namespace, componentType,
        ConcreteMultiValuedAccessorFactory.INSTANCE
    );
  }

  ConcreteArrayOfValuesNode(String name, String elementName, String namespace,
      Class<?> componentType,
      MultiValuedAccessorFactory accessorFactory) {
    super(name, namespace, null);
    this.elementName = elementName;
    this.componentType = componentType;
    this.accessorFactory = accessorFactory;
  }

  @Override
  public String getPropertyName() {
    return getAccessor().getName();
  }

  @Override
  public String getElementName() {
    return elementName;
  }

  @Override
  public Iterator<?> iterator(Object model) throws Exception {
    return getModelIterator(model);
  }

  @Override
  public Class<?> getComponentType() {
    return multiValuedAccessor.getComponentType();
  }

  @Override
  public ToManyAssociationManager<?, ?> getDefaultManager() {
    return multiValuedAccessor;
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitArrayOfValues(this, state);
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.multiValuedAccessor = accessor != null ?
        accessorFactory.newAccessor(accessor, componentType) : null;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return multiValuedAccessor.iterator(source);
  }

}
