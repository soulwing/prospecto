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
 * A visitor for a {@link ViewNode}.
 *
 * @author Carl Harris
 */
public interface ViewNodeVisitor {

  /**
   * Visits a value node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitValue(ValueNode node, Object state);

  /**
   * Visits a URL node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitUrl(UrlNode node, Object state);

  /**
   * Visits an object node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitObject(ObjectNode node, Object state);

  /**
   * Visits a reference node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitReference(ReferenceNode node, Object state);

  /**
   * Visits an envelope node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitEnvelope(EnvelopeNode node, Object state);

  Object visitSubtype(SubtypeNode node, Object state);

  /**
   * Visits an array-of-values node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitArrayOfValues(ArrayOfValuesNode node, Object state);

  /**
   * Visits an array-of-objects node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitArrayOfObjects(ArrayOfObjectsNode node, Object state);

  /**
   * Visits an array-of-references node.
   * @param node the subject node
   * @param state visitor state
   * @return visit result
   */
  Object visitArrayOfReferences(ArrayOfReferencesNode node, Object state);

}
