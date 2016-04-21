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

import java.beans.Introspector;
import java.util.EnumSet;
import java.util.Iterator;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.ArrayOfObjectsNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteMultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;

/**
 * A view node that represents an array of objects.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfObjectsNode extends ConcreteContainerNode
    implements ArrayOfObjectsNode {

  private final MultiValuedAccessorFactory accessorFactory;

  private final String elementName;

  private MultiValuedAccessor multiValuedAccessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param modelType model type of the array elements
   */
  public ConcreteArrayOfObjectsNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    this(name, elementName, namespace, modelType,
        ConcreteMultiValuedAccessorFactory.INSTANCE
    );
  }

  ConcreteArrayOfObjectsNode(String name, String elementName,
      String namespace, Class<?> modelType,
      MultiValuedAccessorFactory accessorFactory) {
    super(name, namespace, modelType);
    this.elementName = elementName;
    this.accessorFactory = accessorFactory;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.multiValuedAccessor = accessorFactory.newAccessor(accessor, getModelType());
  }

  @Override
  public String getPropertyName() {
    return getAccessor().getName();
  }

  @Override
  public Class<?> getComponentType() {
    return multiValuedAccessor.getComponentType();
  }

  @Override
  public String getElementName() {
    if (elementName != null) return elementName;
    return Introspector.decapitalize(getModelType().getSimpleName());
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return multiValuedAccessor != null ?
        multiValuedAccessor.getSupportedModes() : EnumSet.allOf(AccessMode.class);
  }

  @Override
  public ToManyAssociationManager<?, ?> getDefaultManager() {
    return multiValuedAccessor;
  }

  @Override
  public Iterator<?> iterator(Object model) throws Exception {
    return getModelIterator(model);
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return multiValuedAccessor.iterator(source);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitArrayOfObjects(this, state);
  }

}
