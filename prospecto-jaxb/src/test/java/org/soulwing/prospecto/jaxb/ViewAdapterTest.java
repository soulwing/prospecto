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
package org.soulwing.prospecto.jaxb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.view.ViewBuilder;

/**
 * Tests for {@link ViewAdapter}.
 *
 * @author Carl Harris
 */
public class ViewAdapterTest {
  
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

    final JAXBContext jaxbContext = JAXBContext.newInstance(AdaptedModel.class);
    final Marshaller marshaller = jaxbContext.createMarshaller();
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    marshaller.marshal(model, bos);

    final byte[] buf = bos.toByteArray();
    final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    final ByteArrayInputStream bis = new ByteArrayInputStream(buf);
    final AdaptedModel result = (AdaptedModel) unmarshaller.unmarshal(bis);
    assertThat(result.getView(), is(not(nullValue())));

    assertThat(result.getView(), hasEventSequence(
        eventOfType(View.Event.Type.BEGIN_OBJECT,
            withName("myView"), whereValue(is(nullValue()))),
        eventOfType(View.Event.Type.VALUE,
            withName("myValue"), whereValue(is(equalTo("This is a test")))),
        eventOfType(View.Event.Type.END_OBJECT,
            withName("myView"), whereValue(is(nullValue())))
    ));
  }
  
  @XmlRootElement
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class AdaptedModel {

    @XmlElement(name = "myView")
    @XmlJavaTypeAdapter(ViewAdapter.class)
    private View view;

    public View getView() {
      return view;
    }

    public void setView(View view) {
      this.view = view;
    }
  }

}
