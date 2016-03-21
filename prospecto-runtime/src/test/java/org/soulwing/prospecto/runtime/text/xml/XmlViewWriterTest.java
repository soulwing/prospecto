/*
 * File created on Mar 21, 2016
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.LinkedList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.runtime.text.ViewWriterTestBase;

/**
 * Tests for {@link XmlViewWriter}.
 *
 * @author Carl Harris
 */
public class XmlViewWriterTest extends ViewWriterTestBase {

  static class Frame {
    final StringBuilder actualText = new StringBuilder();
    final StringBuilder expectedText = new StringBuilder();
  }

  private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

  private final Deque<Frame> stack = new LinkedList<>();

  public XmlViewWriterTest() {
    super(".xml");
  }

  @Override
  protected ViewWriter newViewWriter(View view, OutputStream outputStream) {
    return new XmlViewWriter(view, outputStream);
  }

  @Override
  protected void validateView(InputStream actual,
      InputStream expected) throws Exception {
    final XMLEventReader expectedReader = inputFactory.createXMLEventReader(
        expected);
    final XMLEventReader actualReader = inputFactory.createXMLEventReader(
        actual);

    while (expectedReader.hasNext()) {
      assertThat(actualReader.hasNext(), is(true));
      final XMLEvent expectedEvent = expectedReader.nextEvent();
      final XMLEvent actualEvent = actualReader.nextEvent();
      switch (expectedEvent.getEventType()) {
        case XMLEvent.START_DOCUMENT:
          assertThat(actualEvent.isStartDocument(), is(true));
          break;
        case XMLEvent.END_DOCUMENT:
          assertThat(actualEvent.isEndDocument(), is(true));
          break;
        case XMLEvent.START_ELEMENT:
          assertThat(actualEvent.isStartElement(), is(true));
          stack.push(new Frame());
          validateStartElement(actualEvent.asStartElement(),
              expectedEvent.asStartElement());
          break;
        case XMLEvent.END_ELEMENT:
          assertThat("expected end but was" + actualEvent,
              actualEvent.isEndElement(), is(true));
          validateEndElement(actualEvent.asEndElement(),
              expectedEvent.asEndElement());
          Frame frame = stack.pop();
          assertThat(frame.actualText.toString().trim(),
              is(equalTo(frame.expectedText.toString().trim())));
          break;
        default:
          break;
      }
      if (expectedEvent.isCharacters()) {
        stack.peek().expectedText.append(expectedEvent.asCharacters().getData());
      }
      if (actualEvent.isCharacters()) {
        stack.peek().actualText.append(actualEvent.asCharacters().getData());
      }
    }
  }

  private void validateStartElement(StartElement actual,
      StartElement expected) {
    assertThat(actual.getName(), is(equalTo(expected.getName())));
    final Attribute expectedType = expected.getAttributeByName(
        XmlViewConstants.XSI_TYPE_QNAME);
    if (expectedType != null) {
      final Attribute actualType = actual.getAttributeByName(
          XmlViewConstants.XSI_TYPE_QNAME);
      assertThat(actualType, is(not(nullValue())));
      assertThat(actualType.getValue(),
          is(equalTo(expectedType.getValue())));
    }
  }

  private void validateEndElement(EndElement actual,
      EndElement expected) {
    assertThat(actual.getName(), is(equalTo(expected.getName())));
  }

}
