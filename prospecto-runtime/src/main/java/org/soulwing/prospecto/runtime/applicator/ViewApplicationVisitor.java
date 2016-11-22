/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import java.util.List;

import org.soulwing.prospecto.api.template.AbstractViewNodeVisitor;
import org.soulwing.prospecto.api.template.ArrayOfObjectsNode;
import org.soulwing.prospecto.api.template.ArrayOfReferencesNode;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.api.template.EnvelopeNode;
import org.soulwing.prospecto.api.template.MapOfObjectsNode;
import org.soulwing.prospecto.api.template.MapOfReferencesNode;
import org.soulwing.prospecto.api.template.MapOfValuesNode;
import org.soulwing.prospecto.api.template.MetaNode;
import org.soulwing.prospecto.api.template.ObjectNode;
import org.soulwing.prospecto.api.template.ReferenceNode;
import org.soulwing.prospecto.api.template.SubtypeNode;
import org.soulwing.prospecto.api.template.ValueNode;

/**
 * A {@link org.soulwing.prospecto.api.template.ViewNodeVisitor} that produces a
 * tree of {@link ViewEventApplicator} nodes.
 *
 * @author Carl Harris
 */
public class ViewApplicationVisitor extends AbstractViewNodeVisitor {

  @Override
  public Object visitValue(ValueNode node, Object state) {
    return new ValueApplicator(node);
  }

  @Override
  public Object visitMeta(MetaNode node, Object state) {
    return new MetaApplicator(node);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitObject(ObjectNode node, Object state) {
    return new ObjectApplicator(node, (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitReference(ReferenceNode node, Object state) {
    return new ReferenceApplicator(node, (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitEnvelope(EnvelopeNode node, Object state) {
    return new EnvelopeApplicator(node, (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitSubtype(SubtypeNode node, Object state) {
    return new SubtypeApplicator(node, (List<ViewEventApplicator>) state);
  }

  @Override
  public Object visitArrayOfValues(ArrayOfValuesNode node, Object state) {
    return new ArrayOfValuesApplicator(node);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitArrayOfObjects(ArrayOfObjectsNode node, Object state) {
    return new ArrayOfObjectsApplicator(node,
        (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitArrayOfReferences(ArrayOfReferencesNode node, Object state) {
    return new ArrayOfReferencesApplicator(node,
        (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitMapOfValues(MapOfValuesNode node, Object state) {
    return new MapOfValuesApplicator(node);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitMapOfObjects(MapOfObjectsNode node, Object state) {
    return new MapOfObjectsApplicator(node,
        (List<ViewEventApplicator>) state);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object visitMapOfReferences(MapOfReferencesNode node, Object state) {
    return new MapOfReferencesApplicator(node,
        (List<ViewEventApplicator>) state);
  }

}
