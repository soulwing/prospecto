package org.soulwing.prospecto.api.factory;

/**
 * A factory that provides a strategy for creating model object instances.
 *
 *
 * @author Carl Harris
 */
public interface ObjectFactory {

  /**
   * Creates a new instance of the given type.
   * <p>
   * An implementation must be able to determine whether it understands how
   * to create an instance of the given type, in such a way that an unrecognized
   * type results in a null return value rather than an exception.
   *
   * @param type the subject type to be instantiated
   * @return object instance or {@code null} to indicate that this factory
   *    does not know how to instantiate the given type
   * @throws Exception if an error occurs in creating an instance of the
   *    given type
   */
  Object newInstance(Class<?> type) throws Exception;

}
