package org.soulwing.prospecto.api.factory;

import java.util.List;

/**
 * A mutable ordered collection of {@link ObjectFactory} instances.
 *
 * @author Carl Harris
 */
public interface ObjectFactories {

  /**
   * Prepends a factory to the collection.
   * @param factory the factory to add
   */
  void prepend(ObjectFactory factory);

  /**
   * Appends a factory to the collection.
   * @param factory the factory to add
   */
  void append(ObjectFactory factory);

  /**
   * Coerces this collection to a {@link List}.
   * <p>
   * The returned list can be manipulated to update this collection of
   * factories.
   *
   * @return factory list
   */
  List<ObjectFactory> toList();

}
