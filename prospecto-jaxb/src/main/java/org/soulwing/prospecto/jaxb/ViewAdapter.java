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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A JAXB {@link XmlAdapter} for a Prospecto {@link View}.
 *
 * @author Carl Harris
 */
public class ViewAdapter extends XmlAdapter<Object, View> {

  private final ViewWriterFactory writerFactory =
      ViewWriterFactoryProducer.getFactory("XML");

  private final ViewReaderFactory readerFactory =
      ViewReaderFactoryProducer.getFactory("XML");

  private final DocumentBuilderFactory builderFactory =
      DocumentBuilderFactory.newInstance();

  private final TransformerFactory transformerFactory =
      TransformerFactory.newInstance();

  public ViewAdapter() {
    builderFactory.setNamespaceAware(true);
  }

  @Override
  public Object marshal(View v) throws Exception {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    writerFactory.newWriter(v, outputStream).writeView();

    final DocumentBuilder builder = builderFactory.newDocumentBuilder();

    final ByteArrayInputStream inputStream = new ByteArrayInputStream(
        outputStream.toByteArray());

    final Document document = builder.parse(inputStream);
    return document.getDocumentElement();
  }

  @Override
  public View unmarshal(Object v) throws Exception {
    final Transformer transformer = transformerFactory.newTransformer();
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    transformer.transform(new DOMSource((Element) v),
        new StreamResult(outputStream));

    final ByteArrayInputStream inputStream = new ByteArrayInputStream(
        outputStream.toByteArray());

    return readerFactory.newReader(inputStream).readView();
  }

}
