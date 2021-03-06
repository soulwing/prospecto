/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.text.xml;

import java.io.InputStream;

import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.runtime.text.Constants;
import org.soulwing.prospecto.runtime.text.ViewReaderTestBase;

/**
 * Tests for {@link XmlViewReader}.
 *
 * @author Carl Harris
 */
public class XmlViewReaderTest extends ViewReaderTestBase {

  public XmlViewReaderTest() {
    super(".xml");
  }

  @Override
  protected ViewReader newViewReader(InputStream inputStream,
      Options options) {
    return new XmlViewReader(inputStream, options);
  }

  @Override
  protected Matcher<View.Event> expectedUrlEvent(String name) {
    return eventWith(View.Event.Type.META, name,
        Constants.URL_VALUE);
  }

}
