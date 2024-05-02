/*
 * File created on Oct 21, 2020
 *
 * Copyright (c) 2020 Carl Harris, Jr
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
package org.soulwing.prospecto.api.json;

import javax.json.JsonStructure;

import org.soulwing.prospecto.Singleton;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.api.splice.SpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * A {@link SpliceHandler} that can be used to embed a
 * {@link javax.json.JsonStructure} into a view.
 *
 * @author Carl Harris
 */
@SuppressWarnings("unused")
public class JsonSpliceHandler implements SpliceHandler {

  private static final String PROVIDER_NAME = "JSON-P";

  private static final Singleton<JsonSpliceHandler> singleton =
      new Singleton<JsonSpliceHandler>() {
        @Override
        protected JsonSpliceHandler newInstance() {
          return new JsonSpliceHandler();
        }
      };

  /**
   * A producer interface for a JSON splice node.
   */
  public interface Producer {
    JsonStructure apply(SpliceNode node, ViewContext context);
  }

  /**
   * A consumer interface for a JSON splice node.
   */
  public interface Consumer {
    Object apply(JsonStructure structure, SpliceNode node, ViewContext context);
  }

  private final ViewReaderFactory readerFactory;
  private final ViewWriterFactory writerFactory;

  /**
   * Gets the singleton instance of this splice handler type.
   * @return splice handler
   */
  public static JsonSpliceHandler getInstance() {
    return singleton.getInstance();
  }

  /**
   * Constructs a new instance.
   */
  private JsonSpliceHandler() {
    final OptionsMap options = new OptionsMap();
    this.readerFactory = ViewReaderFactoryProducer.getFactory(PROVIDER_NAME);
    options.put(WriterKeys.WRAP_ARRAY_IN_ENVELOPE, false);
    options.put(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, false);
    this.writerFactory =
      ViewWriterFactoryProducer.getFactory(PROVIDER_NAME, options);
  }

  @Override
  public View generate(SpliceNode node, ViewContext context) {
    final Producer producer = node.get(Producer.class);
    if (producer == null) {
      throw new ViewTemplateException("node must specify a "
          + Producer.class.getSimpleName() + " attribute");
    }
    final JsonStructure structure = producer.apply(node, context);
    if (structure == null) return null;
    final JsonPSource source = new JsonPSource(structure);
    return readerFactory.newReader(source).readView();
  }

  @Override
  public Object apply(SpliceNode node, View view, ViewContext context)
      throws ViewInputException {
    final Consumer consumer = node.get(Consumer.class);
    if (consumer == null) {
      throw new ViewTemplateException("node must specify a "
          + Consumer.class.getSimpleName() + " attribute");
    }
    final JsonPTarget target = new JsonPTarget();
    writerFactory.newWriter(view).writeView(target);
    return consumer.apply(target.toJson(), node, context);
  }

}
