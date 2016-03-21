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
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.NamespaceContext;
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

import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.text.AbstractViewReader;
import org.soulwing.prospecto.runtime.util.PropertyMap;

/**
 * A {@link org.soulwing.prospecto.api.ViewReader} that parses an XML document.
 *
 * @author Carl Harris
 */
public class XmlViewReader extends AbstractViewReader {

  /**
   * A parser stack frame
   */
  static class Frame {
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
  private final PropertyMap properties;


  public XmlViewReader(InputStream inputStream) {
    this(inputStream, Collections.<String, Object>emptyMap());
  }

  public XmlViewReader(InputStream inputStream,
      Map<String, Object> properties) {
    this.inputStream = inputStream;
    this.properties = new PropertyMap(properties);
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
    final QName type = type(event);
    boolean valueElement = true;
    if (type != null) {
      if (XmlViewConstants.OBJECT_QNAME.equals(type)) {
        beginObject(name(event.getName()));
        valueElement = false;
      }
      else if (XmlViewConstants.ARRAY_QNAME.equals(type)) {
        beginArray(name(event.getName()));
        valueElement = false;
      }
      else if (event.getName().getNamespaceURI().equals(type.getNamespaceURI())) {
        beginObject(name(event.getName()));
        discriminator(type.getLocalPart());
        valueElement = false;
      }
    }

    final Attribute url = event.getAttributeByName(
        new QName(XmlViewConstants.VIEW_NAMESPACE, UrlNode.DEFAULT_NAME));
    if (url != null) {
      url(url.getValue());
    }

    stack.push(new Frame(event.getName(), type, valueElement));
  }

  private void endElement(EndElement event) throws XMLStreamException {
    final Frame frame = stack.pop();
    final String type = frame.getType();
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

  private static QName type(StartElement event) {
    final QName qname = typeToQName(event, event.getNamespaceContext());
    if (qname == null) return null;
    if (event.getName().getNamespaceURI().equals(qname.getNamespaceURI())) {
      return qname;
    }
    if (XmlViewConstants.XS_NAMESPACE.equals(qname.getNamespaceURI())) {
      return qname;
    }
    if (XmlViewConstants.VIEW_NAMESPACE.equals(qname.getNamespaceURI())) {
      return qname;
    }
    return null;
  }

  private static QName typeToQName(StartElement event,
      NamespaceContext namespaceContext) {
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
    return new QName(namespaceContext.getNamespaceURI(prefix), localPart);
  }

  private void value(Frame frame) throws XMLStreamException {
    final String type = frame.getType();
    final String text = frame.getText();
    if (type == null) {
      if (text.isEmpty()) {
        nullValue(frame.getName());
        return;
      }
      throw new XMLStreamException("xsi:type is required");
    }
    switch (type) {
      case XmlViewConstants.XS_INTEGER:
        value(frame.getName(), new BigInteger(text));
        break;
      case XmlViewConstants.XS_DECIMAL:
        value(frame.getName(), new BigDecimal(text));
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
      case XmlViewConstants.XS_BOOLEAN:
        value(frame.getName(), Boolean.valueOf(text));
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

  private static String name(QName qname) {
    if (XmlViewConstants.VIEW_NAMESPACE.equals(qname.getNamespaceURI())) {
      return null;
    }
    return qname.getLocalPart();
  }

}
