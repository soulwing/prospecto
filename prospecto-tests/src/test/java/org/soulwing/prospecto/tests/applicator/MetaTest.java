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
package org.soulwing.prospecto.tests.applicator;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.meta.MetadataHandler;
import org.soulwing.prospecto.api.template.MetaNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.url.UrlResolver;

/**
 * Tests for meta nodes.
 *
 * @author Carl Harris
 */
public class MetaTest extends ViewApplicatorTestBase {

  public static class MockModel {}

  @Test
  public void testSimpleMeta() throws Exception {
    final MetadataHandler handler = new MockMetadataHandler();
    final UrlResolver urlResolver = new MockUrlResolver();
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object("metaTest", MockModel.class)
            .url()
            .meta("foo", handler)
            .end()
        .build();

    context.appendScope().put(urlResolver);
    validate(template);
  }

  private static class MockMetadataHandler implements MetadataHandler {

    private Object value;

    @Override
    public java.lang.Object produceValue(MetaNode node, Object parentModel,
        ViewContext context) throws Exception {
      return value;
    }

    @Override
    public void consumeValue(MetaNode node, Object parentModel,
        Object value, ViewContext context) throws Exception {
      this.value = value;
    }

  }

  private static class MockUrlResolver implements UrlResolver {

    static final String URL = "mockUrl";

    @Override
    public String resolve(ViewNode node, ViewContext context) {
      return URL;
    }

  }

}
