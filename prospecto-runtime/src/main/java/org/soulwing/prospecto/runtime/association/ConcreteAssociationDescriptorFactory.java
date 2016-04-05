/*
 * File created on Mar 31, 2016
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
package org.soulwing.prospecto.runtime.association;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A {@link AssociationDescriptorFactory} implementation.
 *
 * @author Carl Harris
 */
class ConcreteAssociationDescriptorFactory
    implements AssociationDescriptorFactory {

  public static final ConcreteAssociationDescriptorFactory INSTANCE =
      new ConcreteAssociationDescriptorFactory();

  private ConcreteAssociationDescriptorFactory() {}

  @Override
  public AssociationDescriptor newDescriptor(AbstractViewNode node) {
    assertParentIsNotNull(node);
    assertAccessorIsNotNull(node);
    return new ConcreteAssociationDescriptor(node.getParent().getModelType(),
            node.getModelType(), node.getAccessor().getName());
  }

  private void assertParentIsNotNull(AbstractViewNode node) {
    if (node.getParent() != null) return;
    throw new IllegalArgumentException("must be an interior node");
  }

  private void assertAccessorIsNotNull(AbstractViewNode node) {
    if (node.getAccessor() != null) return;
    throw new IllegalArgumentException("accesor is required");
  }

}
