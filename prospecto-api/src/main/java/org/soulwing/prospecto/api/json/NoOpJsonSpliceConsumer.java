package org.soulwing.prospecto.api.json;

import javax.json.JsonStructure;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * A {@link JsonSpliceHandler.Consumer} that does nothing. Useful for situations
 * in which a splice is used to generate JSON but there's no need to consume it.
 */
public class NoOpJsonSpliceConsumer implements JsonSpliceHandler.Consumer {

  public static final NoOpJsonSpliceConsumer INSTANCE = new NoOpJsonSpliceConsumer();

  @Override
  public Object apply(JsonStructure jsonStructure, SpliceNode spliceNode, ViewContext viewContext) {
    return null;
  }

}
