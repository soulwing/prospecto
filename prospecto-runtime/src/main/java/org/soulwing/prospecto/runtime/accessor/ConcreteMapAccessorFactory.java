package org.soulwing.prospecto.runtime.accessor;

/**
 * A concrete {@link MapAccessorFactory} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteMapAccessorFactory implements MapAccessorFactory {

  public static final MapAccessorFactory INSTANCE =
      new ConcreteMapAccessorFactory();

  private ConcreteMapAccessorFactory() {
  }

  @Override
  public MapAccessor newAccessor(Accessor accessor) {
    return new ConcreteMapAccessor(accessor);
  }

}
