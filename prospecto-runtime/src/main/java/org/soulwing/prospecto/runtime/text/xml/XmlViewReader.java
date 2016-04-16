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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.runtime.text.AbstractViewReader;

/**
 * A {@link org.soulwing.prospecto.api.ViewReader} that parses an XML document.
 *
 * @author Carl Harris
 */
class XmlViewReader extends AbstractViewReader {

  public static final String DEFAULT_URL_NAME = ViewDefaults.URL_NAME;

  /**
   * A parser stack frame
   */
  private static class Frame {
    final StringBuilder text = new StringBuilder();

    final QName qname;
    final String type;
    final boolean valueElement;

    Frame(QName qname, QName type, boolean valueElement) {
      this.qname = qname;
      this.type = type != null ? type.getLocalPart() : null;
      this.valueElement = valueElement;
    }

    String getName() {
      if (XmlViewConstants.VIEW_NAMESPACE.equals(qname.getNamespaceURI())) {
        return null;
      }
      return qname.getLocalPart();
    }

    String getType() {
      return type;
    }

    String getText() {
      return text.toString().trim();
    }

    void append(String text) {
      this.text.append(text);
    }

    boolean isValueElement() {
      return valueElement;
    }

  }

  private static final XMLInputFactory inputFactory =
      XMLInputFactory.newFactory();

  private final Deque<Frame> stack = new LinkedList<>();

  private final InputStream inputStream;

  XmlViewReader(InputStream inputStream, Options options) {
    super(options);
    this.inputStream = inputStream;
  }

  @Override
  protected void onReadView() throws Exception {
    final XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);
    while (reader.hasNext()) {
      final XMLEvent event = reader.nextEvent();
      switch (event.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
          startElement(event.asStartElement());
          break;
        case XMLStreamConstants.END_ELEMENT:
          endElement(event.asEndElement());
          break;
        case XMLStreamConstants.CHARACTERS:
          characters(event.asCharacters());
          break;
        default:
          assert true;    // ignore unrecognized events
      }
    }
    reader.close();
  }

  private void startElement(StartElement event) throws XMLStreamException {
    final XmlViewConstants.ElementType elementType = elementType(event);
    final QName type = type(event);
    boolean valueElement = false;
    switch (elementType) {
      case VALUE:
        valueElement = true;
        break;
      case OBJECT:
        beginObject(name(event.getName()), namespace(event.getName()));
        discriminator(event);
        metas(event);
        break;
      case ARRAY:
        beginArray(name(event.getName()), namespace(event.getName()));
        discriminator(event);
        metas(event);
        break;
      default:
        throw new AssertionError("unrecognized element type");
    }

    stack.push(new Frame(event.getName(), type, valueElement));
  }

  private void endElement(EndElement event) throws XMLStreamException {
    final Frame frame = stack.pop();
    if (!frame.isValueElement()) {
      end();
    }
    else {
      value(frame);
    }
  }

  private void characters(Characters event) {
    if (!event.isIgnorableWhiteSpace()) {
      stack.peek().append(event.getData());
    }
  }

  private void discriminator(StartElement event) {
    final Attribute type = event.getAttributeByName(
        XmlViewConstants.XSI_TYPE_QNAME);
    if (type != null) {
      discriminator(type.getValue());
    }
  }

  private void metas(StartElement event) {
    final Iterator attributes = event.getAttributes();
    while (attributes.hasNext()) {
      final Attribute attribute = (Attribute) attributes.next();
      if (XmlViewConstants.META_NAMESPACE.equals(
          attribute.getName().getNamespaceURI())) {
        meta(attribute.getName().getLocalPart(), attribute.getValue());
      }
    }
  }

  private void value(Frame frame) throws XMLStreamException {
    final String type = frame.getType() != null ?
        frame.getType() : XmlViewConstants.XS_STRING;
    final String text = frame.getText();
    if (text.isEmpty()) {
      nullValue(frame.getName());
      return;
    }
    switch (type) {
      case XmlViewConstants.XS_BOOLEAN:
        value(frame.getName(), Boolean.valueOf(text));
        break;
      case XmlViewConstants.XS_BYTE:
        value(frame.getName(), Byte.valueOf(text));
        break;
      case XmlViewConstants.XS_SHORT:
        value(frame.getName(), Short.valueOf(text));
        break;
      case XmlViewConstants.XS_INT:
        value(frame.getName(), Integer.valueOf(text));
        break;
      case XmlViewConstants.XS_LONG:
        value(frame.getName(), Long.valueOf(text));
        break;
      case XmlViewConstants.XS_INTEGER:
        value(frame.getName(), new BigInteger(text));
        break;
      case XmlViewConstants.XS_FLOAT:
        value(frame.getName(), Float.valueOf(text));
        break;
      case XmlViewConstants.XS_DOUBLE:
        value(frame.getName(), Double.valueOf(text));
        break;
      case XmlViewConstants.XS_DECIMAL:
        value(frame.getName(), new BigDecimal(text));
        break;
      case XmlViewConstants.XS_DATE_TIME:
        value(frame.getName(),
          DatatypeConverter.parseDateTime(text).getTime().getTime());
        break;
      case XmlViewConstants.XS_STRING:
        value(frame.getName(), text);
        break;
      default:
        throw new IllegalArgumentException("unrecognized data type: "
            + type);
    }
  }

  private static XmlViewConstants.ElementType elementType(StartElement event) {
    final Attribute attribute = event.getAttributeByName(
        XmlViewConstants.TYPE_QNAME);
    if (attribute == null) return XmlViewConstants.ElementType.VALUE;
    return XmlViewConstants.ElementType.valueOf(
        attribute.getValue().toUpperCase());
  }

  private static QName type(StartElement event) {
    final Attribute type =
        event.getAttributeByName(XmlViewConstants.XSI_TYPE_QNAME);
    if (type == null) return null;
    final String typeName = type.getValue();
    final int index = typeName.indexOf(':');
    if (index == -1) {
      return new QName(event.getName().getNamespaceURI(), typeName);
    }
    final String prefix = typeName.substring(0, index);
    final String localPart = typeName.substring(index + 1);
    return new QName(event.getNamespaceContext().getNamespaceURI(prefix),
        localPart);
  }

  private static String name(QName qname) {
    if (XmlViewConstants.VIEW_NAMESPACE.equals(qname.getNamespaceURI())) {
      return null;
    }
    return qname.getLocalPart();
  }

  private static String namespace(QName qname) {
    if (XmlViewConstants.VIEW_NAMESPACE.equals(qname.getNamespaceURI())) {
      return null;
    }
    return qname.getNamespaceURI();
  }

}
