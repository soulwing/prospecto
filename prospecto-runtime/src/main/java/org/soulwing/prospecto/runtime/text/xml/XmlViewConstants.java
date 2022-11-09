/*
 * File created on Mar 20, 2016
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

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Constants used in reading/writing XML views.
 *
 * @author Carl Harris
 */
public interface XmlViewConstants {

  enum ElementType {
    VALUE,
    OBJECT,
    ARRAY,
  }

  String VIEW_NAMESPACE = "urn:prospecto.soulwing.org:view";

  String META_NAMESPACE = "urn:prospecto.soulwing.org:meta";

  String DEFAULT_NAMESPACE = "urn:prospecto.soulwing.org:default";

  String XS_NAMESPACE = XMLConstants.W3C_XML_SCHEMA_NS_URI;

  String XSI_NAMESPACE = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

  String TYPE_NAME = "type";

  String NULL_NAME = "null";

  String VALUE_NAME = ElementType.VALUE.name().toLowerCase();

  String OBJECT_NAME = ElementType.OBJECT.name().toLowerCase();

  String ARRAY_NAME = ElementType.ARRAY.name().toLowerCase();

  String VIEW_NAME = "view";

  QName TYPE_QNAME = new QName(VIEW_NAMESPACE, TYPE_NAME);

  QName NULL_QNAME = new QName(VIEW_NAMESPACE, NULL_NAME);

  QName VALUE_QNAME = new QName(VIEW_NAMESPACE, VALUE_NAME);

  QName OBJECT_QNAME = new QName(VIEW_NAMESPACE, OBJECT_NAME);

  QName ARRAY_QNAME = new QName(VIEW_NAMESPACE, ARRAY_NAME);

  QName VIEW_QNAME = new QName(VIEW_NAMESPACE, VIEW_NAME);

  String XSI_TYPE_NAME = "type";

  QName XSI_TYPE_QNAME = new QName(XSI_NAMESPACE, XSI_TYPE_NAME);

  String XS_BOOLEAN = "boolean";

  String XS_BYTE = "byte";

  String XS_SHORT = "short";

  String XS_INT = "int";

  String XS_LONG = "long";

  String XS_INTEGER = "integer";

  String XS_FLOAT = "float";

  String XS_DOUBLE = "double";

  String XS_DECIMAL = "decimal";

  String XS_DATE_TIME = "dateTime";

  String XS_STRING = "string";

}
