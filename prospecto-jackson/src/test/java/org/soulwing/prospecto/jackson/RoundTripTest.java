/*
 * File created on Apr 27, 2016
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
package org.soulwing.prospecto.jackson;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withNoName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.view.ViewBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Tests of the serializer and deserializer.
 *
 * @author Carl Harris
 */
public class RoundTripTest {

  @Test
  public void test() throws Exception {
    final View view = ViewBuilder.begin()
        .type(View.Event.Type.BEGIN_OBJECT)
        .name("myView")
        .type(View.Event.Type.VALUE)
        .name("myValue")
        .value("This is a test")
        .type(View.Event.Type.END_OBJECT)
        .name("myView")
        .end();

    final AdaptedModel model = new AdaptedModel();
    model.setView(view);

    final ObjectMapper mapper = new ObjectMapper();
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    mapper.writeValue(bos, model);

    final byte[] buf = bos.toByteArray();

    System.out.write(buf);
    System.out.println();

    final ByteArrayInputStream bis = new ByteArrayInputStream(buf);
    final AdaptedModel result = mapper.readValue(bis, AdaptedModel.class);

    assertThat(result.getView(), is(not(nullValue())));

    assertThat(result.getView(), hasEventSequence(
        eventOfType(View.Event.Type.BEGIN_OBJECT,
            withNoName(), whereValue(is(nullValue()))),
        eventOfType(View.Event.Type.VALUE,
            withName("myValue"), whereValue(is(equalTo("This is a test")))),
        eventOfType(View.Event.Type.END_OBJECT,
            withNoName(), whereValue(is(nullValue())))
    ));
  }

  public static class AdaptedModel {

    @JsonProperty("myView")
    @JsonSerialize(using = ViewSerializer.class)
    @JsonDeserialize(using = ViewDeserializer.class)
    private View view;

    public View getView() {
      return view;
    }

    public void setView(View view) {
      this.view = view;
    }
  }

}
