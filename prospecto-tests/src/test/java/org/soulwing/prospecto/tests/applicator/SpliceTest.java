/*
 * File created on Mar 24, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
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
package org.soulwing.prospecto.tests.applicator;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.splice.SpliceHandler;
import org.soulwing.prospecto.api.splice.ViewGeneratingSpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 *
 *
 * @author Carl Harris
 */
public class SpliceTest extends ViewApplicatorTestBase {

  private final SpliceHandler handler = new ViewGeneratingSpliceHandler() {

    @Override
    protected Object getRoot(SpliceNode node, ViewContext context) {
      return context.get(OtherModel.class);
    }

    @Override
    public void apply(SpliceNode node, View view, ViewContext context)
        throws ViewInputException {
      node.get(ViewTemplate.class).createApplicator(view, context)
          .update(context.get(OtherModel.class));
    }
  };

  @Test
  public void testSplice() throws Exception {
    final OtherModel otherModel = new OtherModel();
    final ViewTemplate otherTemplate = ViewTemplateBuilderProducer
        .object(OtherModel.class)
            .accessType(AccessType.FIELD)
            .value("myProperty")
            .end()
        .build();

    final ViewTemplate rootTemplate = ViewTemplateBuilderProducer
        .object("spliceTest", RootModel.class)
          .splice("child", handler)
              .attribute(otherTemplate)
          .end()
        .build();

    context.appendScope().put(otherModel);
    validate(rootTemplate);
  }


  public static class RootModel {
  }

  public static class OtherModel {
    String myProperty;
  }

}
