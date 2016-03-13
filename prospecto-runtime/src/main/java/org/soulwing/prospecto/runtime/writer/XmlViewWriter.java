/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.writer;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;

/**
 * A {@link ViewWriter} that produces an XML representation of a view.
 * <p>
 * Views produced by this writer should generally include a name nad
 * namespace URI at the root level, and element names for array elements.
 * However, reasonable defaults will be used in their absence.
 * <p>
 * View envelope properties will be added as to the root element of the view
 * as attributes in the {@link #DEFAULT_NAMESPACE}.
 *
 * @author Carl Harris
 */
class XmlViewWriter extends AbstractViewWriter {

  private static final String XML_VERSION = "1.0";

  public static final String DEFAULT_ENCODING = "UTF-8";

  public static final String DEFAULT_NAMESPACE = "urn:org.soulwing.prospecto:view";

  private static final QName DEFAULT_VIEW_NAME =
      new QName(DEFAULT_NAMESPACE, "view");

  private static final QName DEFAULT_OBJECT_NAME =
      new QName(DEFAULT_NAMESPACE, "object");

  private static final QName DEFAULT_ARRAY_NAME =
      new QName(DEFAULT_NAMESPACE, "array");

  private static final QName DEFAULT_VALUE_NAME =
      new QName(DEFAULT_NAMESPACE, "value");

  private static final QName DEFAULT_NULL_NAME =
      new QName(DEFAULT_NAMESPACE, "null");

  private static final XMLOutputFactory outputFactory =
      XMLOutputFactory.newFactory();

  private final Deque<String> namespaceStack = new LinkedList<>();

  private final String encoding;

  private XMLStreamWriter writer;

  private boolean firstEvent = true;

  /**
   * Constructs a new instance.
   * @param view source view
   * @param outputStream target output stream for the textual representation
   */
  public XmlViewWriter(View view,
      OutputStream outputStream) {
    this(view, outputStream, DEFAULT_ENCODING);
  }

  /**
   * Constructs a new instance.
   * @param view source view
   * @param outputStream target output stream for the textual representation
   * @param encoding character encoding for the XML
   */
  public XmlViewWriter(View view, OutputStream outputStream, String encoding) {
    super(view, outputStream);
    this.encoding = encoding;
  }

  @Override
  protected void beforeViewEvents(OutputStream outputStream) throws Exception {
    writer = new IndentingXMLStreamWriter(
          outputFactory.createXMLStreamWriter(
              new BufferedOutputStream(outputStream)));
  }

  @Override
  protected void afterViewEvents() throws Exception {
    writer.flush();
  }

  @Override
  protected void onBeginObject(View.Event event) throws Exception {
    writeStartElement(event);
  }

  @Override
  protected void onEndObject(View.Event event) throws Exception {
    writeEndElement();
  }

  @Override
  protected void onBeginArray(View.Event event) throws Exception {
    writeStartElement(event);
  }

  @Override
  protected void onEndArray(View.Event event) throws Exception {
    writeEndElement();
  }

  @Override
  protected void onValue(View.Event event) throws Exception {
    writeValue(event);
  }

  @Override
  protected void onUrl(View.Event event) throws Exception {
    writeAttribute(event);
  }

  private void writeStartElement(View.Event event)
      throws XMLStreamException {
    if (firstEvent) {
      writeRootElement(event);
      firstEvent = false;
    }
    else {
      writeStartElement(event.getName(), event.getNamespace(),
          event.getType() == View.Event.Type.BEGIN_OBJECT ?
              DEFAULT_OBJECT_NAME : DEFAULT_ARRAY_NAME);
    }
  }

  private void writeRootElement(View.Event event)
      throws XMLStreamException {
    namespaceStack.push(event.getNamespace() != null ?
        event.getNamespace() : DEFAULT_NAMESPACE);

    writer.setDefaultNamespace(namespaceStack.peek());
    writer.setPrefix("v", DEFAULT_NAMESPACE);
    writer.writeStartDocument(encoding, XML_VERSION);
    writeStartElement(event.getName(), namespaceStack.peek(), DEFAULT_VIEW_NAME);
    writer.writeDefaultNamespace(namespaceStack.peek());
    writer.writeNamespace("v", DEFAULT_NAMESPACE);
    for (Map.Entry<String, Object> entry : getView().getEnvelope()) {
      writer.writeAttribute(writer.getNamespaceContext().getNamespaceURI("v"),
          entry.getKey(), entry.getValue().toString());
    }

  }

  private void writeValue(View.Event event) throws XMLStreamException {
    final String name = event.getName();
    final String namespace = event.getNamespace();
    final Object value = event.getValue();
    if (value instanceof BigDecimal) {
      writeString(name, namespace, DatatypeConverter.printDecimal((BigDecimal) value));
    }
    else if (value instanceof BigInteger) {
      writeString(name, namespace, DatatypeConverter.printInteger((BigInteger) value));
    }
    else if (value instanceof Byte) {
      writeString(name, namespace, DatatypeConverter.printByte((Byte) value));
    }
    else if (value instanceof Double) {
      writeString(name, namespace, DatatypeConverter.printDouble((Double) value));
    }
    else if (value instanceof Float) {
      writeString(name, namespace, DatatypeConverter.printFloat((Float) value));
    }
    else if (value instanceof Short) {
      writeString(name, namespace, DatatypeConverter.printShort((Short) value));
    }
    else if (value instanceof Integer) {
      writeString(name, namespace, DatatypeConverter.printInt((Integer) value));
    }
    else if (value instanceof Long) {
      writeString(name, namespace, DatatypeConverter.printLong((Long) value));
    }
    else if (value instanceof Boolean) {
      writeString(name, namespace, DatatypeConverter.printBoolean((Boolean) value));
    }
    else if (value instanceof Date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime((Date) value);
      writeString(name, namespace, DatatypeConverter.printDateTime(calendar));
    }
    else if (value instanceof Calendar) {
      writeString(name, namespace, DatatypeConverter.printDateTime((Calendar) value));
    }
    else if (value == null) {
      writeEmptyElement(name, namespace, DEFAULT_NULL_NAME);
    }
    else {
      writeString(name, namespace, value.toString());
    }
  }

  private void writeString(String name, String namespace, String value)
      throws XMLStreamException {
    writeStartElement(name, namespace, DEFAULT_VALUE_NAME);
    writer.writeCharacters(value);
    writer.writeEndElement();
  }

  private void writeAttribute(View.Event event) throws XMLStreamException {
    writeAttributeString(event.getName(),
        event.getNamespace() != null ? event.getNamespace() : DEFAULT_NAMESPACE,
        event.getValue().toString());
  }

  private void writeAttributeString(String name, String namespace, String value)
      throws XMLStreamException {
    if (namespace != null && !namespaceStack.peek().equals(namespace)) {
      writer.writeAttribute(namespace, name, value);
    }
    else {
      writer.writeAttribute(name, value);
    }
  }

  private void writeStartElement(String name, String namespace,
      QName defaultName) throws XMLStreamException {
    if (name == null) {
      writer.writeStartElement(defaultName.getNamespaceURI(),
          defaultName.getLocalPart());
    }
    else if (namespace != null && !namespaceStack.peek().equals(namespace)) {
      writer.setDefaultNamespace(namespace);
      writer.writeStartElement(namespace, name);
      writer.writeDefaultNamespace(namespace);
      namespaceStack.push(namespace);
    }
    else {
      writer.writeStartElement(name);
      namespaceStack.push(namespaceStack.peek());
    }
  }

  private void writeEmptyElement(String name, String namespace,
      QName defaultName) throws XMLStreamException {
    if (name == null) {
      writer.writeEmptyElement(defaultName.getNamespaceURI(),
          defaultName.getLocalPart());
    }
    else if (namespace != null && !namespaceStack.peek().equals(namespace)) {
      writer.writeEmptyElement(namespace, name);
    }
    else {
      writer.writeEmptyElement(name);
    }
  }

  private void writeEndElement() throws XMLStreamException {
    writer.writeEndElement();
    writer.setDefaultNamespace(namespaceStack.pop());
  }

  static class IndentingXMLStreamWriter implements XMLStreamWriter {

    private static final int WIDTH = 2;
    private static final char INDENT_CHAR = ' ';
    private static final char LINEFEED_CHAR = '\n';

    private final XMLStreamWriter delegate;

    private final Map<Integer, Boolean> hasChildElement = new HashMap<>();

    private int depth = 0;

    public IndentingXMLStreamWriter(XMLStreamWriter delegate) {
      this.delegate = delegate;
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
      handleStartElement();
      delegate.writeStartElement(localName);
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName)
        throws XMLStreamException {
      handleStartElement();
      delegate.writeStartElement(namespaceURI, localName);
    }

    @Override
    public void writeStartElement(String prefix, String localName,
        String namespaceURI) throws XMLStreamException {
      handleStartElement();
      delegate.writeStartElement(prefix, localName, namespaceURI);
    }

    @Override
    public void writeEmptyElement(String namespaceURI, String localName)
        throws XMLStreamException {
      handleEmptyElement();
      delegate.writeEmptyElement(namespaceURI, localName);
    }

    @Override
    public void writeEmptyElement(String prefix, String localName,
        String namespaceURI) throws XMLStreamException {
      handleEmptyElement();
      delegate.writeEmptyElement(prefix, localName, namespaceURI);
    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
      handleEmptyElement();
      delegate.writeEmptyElement(localName);
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
      handleEndElement();
      delegate.writeEndElement();
    }

    @Override
    public void writeEndDocument() throws XMLStreamException {
      delegate.writeEndDocument();
    }

    @Override
    public void close() throws XMLStreamException {
      delegate.close();
    }

    @Override
    public void flush() throws XMLStreamException {
      delegate.flush();
    }

    @Override
    public void writeAttribute(String localName, String value)
        throws XMLStreamException {
      delegate.writeAttribute(localName, value);
    }

    @Override
    public void writeAttribute(String prefix, String namespaceURI,
        String localName, String value) throws XMLStreamException {
      delegate.writeAttribute(prefix, namespaceURI, localName, value);
    }

    @Override
    public void writeAttribute(String namespaceURI, String localName,
        String value) throws XMLStreamException {
      delegate.writeAttribute(namespaceURI, localName, value);
    }

    @Override
    public void writeNamespace(String prefix, String namespaceURI)
        throws XMLStreamException {
      delegate.writeNamespace(prefix, namespaceURI);
    }

    @Override
    public void writeDefaultNamespace(String namespaceURI)
        throws XMLStreamException {
      delegate.writeDefaultNamespace(namespaceURI);
    }

    @Override
    public void writeComment(String data) throws XMLStreamException {
      delegate.writeComment(data);
    }

    @Override
    public void writeProcessingInstruction(String target)
        throws XMLStreamException {
      delegate.writeProcessingInstruction(target);
    }

    @Override
    public void writeProcessingInstruction(String target, String data)
        throws XMLStreamException {
      delegate.writeProcessingInstruction(target, data);
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
      delegate.writeCData(data);
    }

    @Override
    public void writeDTD(String dtd) throws XMLStreamException {
      delegate.writeDTD(dtd);
    }

    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
      delegate.writeEntityRef(name);
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
      delegate.writeStartDocument();
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
      delegate.writeStartDocument(version);
    }

    @Override
    public void writeStartDocument(String encoding, String version)
        throws XMLStreamException {
      delegate.writeStartDocument(encoding, version);
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
      delegate.writeCharacters(text);
    }

    @Override
    public void writeCharacters(char[] text, int start, int len)
        throws XMLStreamException {
      delegate.writeCharacters(text, start, len);
    }

    @Override
    public String getPrefix(String uri) throws XMLStreamException {
      return delegate.getPrefix(uri);
    }

    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
      delegate.setPrefix(prefix, uri);
    }

    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
      delegate.setDefaultNamespace(uri);
    }

    @Override
    public void setNamespaceContext(NamespaceContext context)
        throws XMLStreamException {
      delegate.setNamespaceContext(context);
    }

    @Override
    public NamespaceContext getNamespaceContext() {
      return delegate.getNamespaceContext();
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
      return delegate.getProperty(name);
    }

    private void handleStartElement() throws XMLStreamException {
      // update state of parent node
      if (depth > 0) {
        hasChildElement.put(depth - 1, true);
      }
      // reset state of current node
      hasChildElement.put(depth, false);
      // indent for current depth
      indent(depth);

      depth++;
    }

    private void handleEmptyElement() throws XMLStreamException {
      // update state of parent node
      if (depth > 0) {
        hasChildElement.put(depth - 1, true);
      }
      indent(depth);
    }

    private void handleEndElement() throws XMLStreamException {
      depth--;
      if (hasChildElement.get(depth)) {
        indent(depth);
      }
    }

    private void indent(int level) throws XMLStreamException {
      char[] buf = new char[WIDTH*level + 1];
      Arrays.fill(buf, INDENT_CHAR);
      buf[0] = LINEFEED_CHAR;
      delegate.writeCharacters(buf, 0, buf.length);
    }

  }

}
