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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.api.text.OutputStreamTarget;
import org.soulwing.prospecto.runtime.text.AbstractViewWriter;

/**
 * A {@link ViewWriter} that produces an XML representation of a view.
 * <p>
 * Views produced by this writer should generally include a name nad
 * namespace URI at the root level, and element names for array elements.
 * However, reasonable defaults will be used in their absence.
 * <p>
 * View envelope properties will be added as to the root element of the view
 * as attributes in the {@link XmlViewConstants#VIEW_NAMESPACE}.
 *
 * @author Carl Harris
 */
class XmlViewWriter extends AbstractViewWriter {

  private static final String XML_VERSION = "1.0";

  public static final String DEFAULT_ENCODING = "UTF-8";

  private static final XMLOutputFactory outputFactory =
      XMLOutputFactory.newFactory();

  private static final String VIEW_PREFIX = "v";
  private static final String META_PREFIX = "m";
  private static final String XS_PREFIX = "xs";
  private static final String XSI_PREFIX = "xsi";

  private static final String XS_BOOLEAN = pname(XS_PREFIX,
      XmlViewConstants.XS_BOOLEAN);

  private static final String XS_BYTE = pname(XS_PREFIX,
      XmlViewConstants.XS_BYTE);

  private static final String XS_SHORT = pname(XS_PREFIX,
      XmlViewConstants.XS_SHORT);

  private static final String XS_INT = pname(XS_PREFIX,
      XmlViewConstants.XS_INT);

  private static final String XS_LONG = pname(XS_PREFIX,
      XmlViewConstants.XS_LONG);

  private static final String XS_INTEGER = pname(XS_PREFIX,
      XmlViewConstants.XS_INTEGER);

  private static final String XS_FLOAT = pname(XS_PREFIX,
      XmlViewConstants.XS_FLOAT);

  private static final String XS_DOUBLE = pname(XS_PREFIX,
      XmlViewConstants.XS_FLOAT);

  private static final String XS_DECIMAL = pname(XS_PREFIX,
      XmlViewConstants.XS_DECIMAL);

  private static final String XS_DATE_TIME = pname(XS_PREFIX,
      XmlViewConstants.XS_DATE_TIME);

  private static final String XS_STRING = pname(XS_PREFIX,
      XmlViewConstants.XS_STRING);

  private static final boolean OPTIONAL_XSI_TYPE = false;
  private static final boolean MANDATORY_XSI_TYPE = true;

  private final Deque<String> namespaceStack = new LinkedList<>();

  private OutputStream outputStream;
  private String encoding;

  private XMLStreamWriter writer;

  private boolean firstEvent = true;

  /**
   * Constructs a new instance.
   * @param view source view
   * @param outputStream target output stream for the textual representation
   * @param options configuration options
   */
  XmlViewWriter(View view,
      OutputStream outputStream, Options options) {
    this(view, outputStream, options, null);
  }

  /**
   * Constructs a new instance.
   * @param view source view
   * @param outputStream target output stream for the textual representation
   * @param options configuration options
   * @param encoding character encoding for the XML
   */
  XmlViewWriter(View view,
      OutputStream outputStream, Options options, String encoding) {
    this(view, options);
    this.outputStream = outputStream;
    this.encoding = encoding;
  }

  /**
   * Constructs a new instance.
   * @param view source view
   * @param options configuration options
   */
  XmlViewWriter(View view, Options options) {
    super(view, options);
  }

  @Override
  public void writeView(Target target) {
    if (!(target instanceof OutputStreamTarget)) {
      throw new IllegalArgumentException("this writer supports only the "
          + OutputStreamTarget.class.getSimpleName() + " target");
    }
    this.outputStream = ((OutputStreamTarget) target).getOutputStream();
    this.encoding = ((OutputStreamTarget) target).getEncoding();
    writeView();
  }


  @Override
  protected void beforeViewEvents() throws Exception {
    writer = outputFactory.createXMLStreamWriter(
        new BufferedOutputStream(outputStream),
            encoding == null ? DEFAULT_ENCODING : encoding);

    if (getOptions().isEnabled(WriterKeys.PRETTY_PRINT_OUTPUT))
      writer = new IndentingXMLStreamWriter(writer);
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
  protected void onMeta(View.Event event) throws Exception {
    writeMeta(event);
  }

  @Override
  protected void onDiscriminator(View.Event event) throws Exception {
    writeDiscriminator(event);
  }

  private void writeStartElement(View.Event event)
      throws XMLStreamException {
    if (firstEvent) {
      writeRootElement(event);
      firstEvent = false;
    }
    else {
      writeStartElement(event.getName(), event.getNamespace(), event.getType());
    }
  }

  private void writeStartElement(String name, String namespace,
      View.Event.Type type) throws XMLStreamException {
    writeStartElement(name, namespace,
        type == View.Event.Type.BEGIN_OBJECT ?
            XmlViewConstants.OBJECT_QNAME : XmlViewConstants.ARRAY_QNAME);
    writeElementType(type);
  }

  private void writeRootElement(View.Event event)
      throws XMLStreamException {
    namespaceStack.push(event.getNamespace() != null ?
        event.getNamespace() : XmlViewConstants.DEFAULT_NAMESPACE);

    writer.setDefaultNamespace(namespaceStack.peek());
    writer.setPrefix(VIEW_PREFIX, XmlViewConstants.VIEW_NAMESPACE);
    writer.setPrefix(META_PREFIX, XmlViewConstants.META_NAMESPACE);
    writer.setPrefix(XSI_PREFIX, XmlViewConstants.XSI_NAMESPACE);
    writer.setPrefix(XS_PREFIX, XmlViewConstants.XS_NAMESPACE);
    writer.writeStartDocument(encoding, XML_VERSION);
    writeStartElement(event.getName(), namespaceStack.peek(),
        event.getType() == View.Event.Type.BEGIN_OBJECT ?
            XmlViewConstants.OBJECT_QNAME : XmlViewConstants.ARRAY_QNAME);
    writer.writeDefaultNamespace(namespaceStack.peek());
    writer.writeNamespace(VIEW_PREFIX, XmlViewConstants.VIEW_NAMESPACE);
    writer.writeNamespace(META_PREFIX, XmlViewConstants.META_NAMESPACE);
    writer.writeNamespace(XSI_PREFIX, XmlViewConstants.XSI_NAMESPACE);
    writer.writeNamespace(XS_PREFIX, XmlViewConstants.XS_NAMESPACE);
    writeElementType(event);
    for (Map.Entry<String, Object> entry : getView().envelope()) {
      writer.writeAttribute(writer.getNamespaceContext().getNamespaceURI(VIEW_PREFIX),
          entry.getKey(), entry.getValue().toString());
    }
  }

  private void writeElementType(View.Event event) throws XMLStreamException {
    writeElementType(event.getType());
  }

  private void writeElementType(View.Event.Type eventType) throws XMLStreamException {
    writer.writeAttribute(XmlViewConstants.VIEW_NAMESPACE,
        XmlViewConstants.TYPE_NAME,
        eventType == View.Event.Type.BEGIN_OBJECT ?
            XmlViewConstants.OBJECT_NAME : XmlViewConstants.ARRAY_NAME);
  }

  private void writeValue(View.Event event) throws XMLStreamException {
    final String name = event.getName();
    final String namespace = event.getNamespace();
    final Object value = event.getValue();
    writeValue(name, namespace, value, OPTIONAL_XSI_TYPE);
  }

  private void writeValue(String name, String namespace, Object value,
      boolean includeXsiType) throws XMLStreamException {
    if (value instanceof Boolean) {
      writeString(name, namespace, XS_BOOLEAN, DatatypeConverter.printBoolean((Boolean) value), includeXsiType);
    }
    else if (value instanceof Integer) {
      writeString(name, namespace, XS_INT, DatatypeConverter.printInt((Integer) value), includeXsiType);
    }
    else if (value instanceof Long) {
      writeString(name, namespace, XS_LONG, DatatypeConverter.printLong((Long) value), includeXsiType);
    }
    else if (value instanceof Double) {
      writeString(name, namespace, XS_DOUBLE, DatatypeConverter.printDouble((Double) value), includeXsiType);
    }
    else if (value instanceof Float) {
      writeString(name, namespace, XS_FLOAT, DatatypeConverter.printFloat((Float) value), includeXsiType);
    }
    else if (value instanceof Byte) {
      writeString(name, namespace, XS_BYTE, DatatypeConverter.printByte((Byte) value), includeXsiType);
    }
    else if (value instanceof Short) {
      writeString(name, namespace, XS_SHORT, DatatypeConverter.printShort((Short) value), includeXsiType);
    }
    else if (value instanceof BigInteger) {
      writeString(name, namespace, XS_INTEGER, DatatypeConverter.printInteger((BigInteger) value), includeXsiType);
    }
    else if (value instanceof BigDecimal) {
      writeString(name, namespace, XS_DECIMAL, DatatypeConverter.printDecimal((BigDecimal) value), includeXsiType);
    }
    else if (value instanceof Date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime((Date) value);
      writeString(name, namespace, XS_DATE_TIME, DatatypeConverter.printDateTime(calendar), includeXsiType);
    }
    else if (value instanceof Calendar) {
      writeString(name, namespace, XS_DATE_TIME, DatatypeConverter.printDateTime((Calendar) value), includeXsiType);
    }
    else if (value instanceof Enum) {
      writeString(name, namespace, XS_STRING, ((Enum) value).name(), includeXsiType);
    }
    else if (value instanceof JsonStructure) {
      writeJsonStructure(name, namespace, (JsonStructure) value);
    }
    else if (value == null || value.toString() == null) {
      writeEmptyElement(name, namespace, XmlViewConstants.NULL_QNAME);
    }
    else {
      writeString(name, namespace, XS_STRING, value.toString(), includeXsiType);
    }
  }

  private void writeJsonStructure(String name, String namespace,
      JsonStructure value) throws XMLStreamException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Json.createWriter(outputStream).write(value);
    final JsonParser parser =
        Json.createParser(new ByteArrayInputStream(outputStream.toByteArray()));

    while (parser.hasNext()) {
      final JsonParser.Event event = parser.next();
      switch (event) {
        case KEY_NAME:
          name = parser.getString();
          break;
        case START_OBJECT:
          writeStartElement(name, namespace, View.Event.Type.BEGIN_OBJECT);
          break;
        case START_ARRAY:
          writeStartElement(name, namespace, View.Event.Type.BEGIN_ARRAY);
          break;
        case END_OBJECT:
        case END_ARRAY:
          writeEndElement();
          break;
        case VALUE_NULL:
        case VALUE_FALSE:
        case VALUE_TRUE:
        case VALUE_NUMBER:
        case VALUE_STRING:
          writeValue(name, namespace, jsonValue(parser.getValue()),
              MANDATORY_XSI_TYPE);
          break;
        default:
          throw new IllegalStateException("unrecognized parser event");
      }
    }

  }

  private Object jsonValue(JsonValue value) {
    if (value == JsonValue.NULL) {
      return null;
    }
    if (value == JsonValue.TRUE) {
      return true;
    }
    if (value == JsonValue.FALSE) {
      return false;
    }
    if (value instanceof JsonString) {
      return ((JsonString) value).getString();
    }
    if (value instanceof JsonNumber) {
      final JsonNumber number = (JsonNumber) value;
      if (number.isIntegral()) {
        return number.bigIntegerValue();
      }
      else {
        return number.bigDecimalValue();
      }
    }
    throw new IllegalStateException("unrecognized JSON data type");
  }

  private void writeString(String name, String namespace, String type,
      String value, boolean includeXsiType) throws XMLStreamException {
    writeStartElement(name, namespace, XmlViewConstants.VALUE_QNAME);
    if (includeXsiType
        || getOptions().isEnabled(WriterKeys.INCLUDE_XML_XSI_TYPE)) {
      writer.writeAttribute(XmlViewConstants.XSI_NAMESPACE,
          XmlViewConstants.XSI_TYPE_NAME, type);
    }
    writer.writeCharacters(value);
    writer.writeEndElement();
  }

  private void writeMeta(View.Event event) throws XMLStreamException {
    if (event.getValue() == null) {
      writeEmptyElement(event.getName(),
          XmlViewConstants.META_NAMESPACE, XmlViewConstants.NULL_QNAME);
    }
    else {
      writeAttributeString(event.getName(),
          XmlViewConstants.META_NAMESPACE, event.getValue().toString());
    }
  }

  private void writeDiscriminator(View.Event event) throws XMLStreamException {
    writeAttributeString(XmlViewConstants.XSI_TYPE_NAME,
        XmlViewConstants.XSI_NAMESPACE, event.getValue().toString());
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

  private static String pname(String prefix, String name) {
    return prefix + ":" + name;
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
