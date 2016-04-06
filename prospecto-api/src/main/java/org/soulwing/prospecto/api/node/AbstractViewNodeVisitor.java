/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.api.node;

/**
 * An abstract base for a {@link ViewNodeVisitor} implementation.
 * <p>
 * This type can be subclassed to implement a {@link ViewNodeVisitor}. It
 * provides default implementations of the interface methods that do nothing
 * aside from returning the provided state.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewNodeVisitor implements ViewNodeVisitor {

  @Override
  public Object visitValue(ValueNode node, Object state) {
    return state;
  }

  @Override
  public Object visitUrl(UrlNode node, Object state) {
    return state;
  }

  @Override
  public Object visitObject(ObjectNode node, Object state) {
    return state;
  }

  @Override
  public Object visitReference(ReferenceNode node, Object state) {
    return state;
  }

  @Override
  public Object visitEnvelope(EnvelopeNode node, Object state) {
    return state;
  }

  @Override
  public Object visitArrayOfValues(ArrayOfValuesNode node, Object state) {
    return state;
  }

  @Override
  public Object visitArrayOfObjects(ArrayOfObjectsNode node, Object state) {
    return state;
  }

  @Override
  public Object visitArrayOfReferences(ArrayOfReferencesNode node, Object state) {
    return state;
  }

}
