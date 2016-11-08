package org.soulwing.prospecto.runtime.factory;

import org.soulwing.prospecto.api.factory.ObjectFactory;
import org.soulwing.prospecto.runtime.util.SimpleLinkedList;

/**
 * An {@link ObjectFactoryService} implemented using a linked list of
 * {@link ObjectFactory} instances as strategies.
 *
 * @author Carl Harris
 */
public class LinkedListObjectFactoryService
    extends SimpleLinkedList<ObjectFactory> implements ObjectFactoryService {

  @Override
  public Object newInstance(Class<?> type) throws Exception {
    for (final ObjectFactory strategy : toList()) {
      final Object ref = strategy.newInstance(type);
      if (ref != null) return ref;
    }
    return type.newInstance();
  }

}
