/*
 * File created on Apr 2, 2016
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

import org.soulwing.prospecto.api.node.ArrayOfReferencesNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.association.ReferenceCollectionToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that represents an array of references.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfReferencesNode extends ConcreteArrayOfObjectsNode
    implements ArrayOfReferencesNode {

  private final ToManyAssociationUpdater associationUpdater;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param modelType model type of the array elements
   */
  public ConcreteArrayOfReferencesNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    this(name, elementName, namespace, modelType,
        ReferenceCollectionToManyAssociationUpdater.INSTANCE);
  }

  ConcreteArrayOfReferencesNode(String name, String elementName, String namespace,
      Class<?> modelType, ToManyAssociationUpdater associationUpdater) {
    super(name, elementName, namespace, modelType);
    this.associationUpdater = associationUpdater;
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitArrayOfReferences(this, state);
  }

  @Override
  public void inject(Object target, Object value) {}

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    associationUpdater.update(this, target, (Iterable<?>) value,
        getMultiValuedAccessor(), context);
  }

}
