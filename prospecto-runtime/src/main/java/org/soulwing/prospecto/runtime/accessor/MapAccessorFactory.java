package org.soulwing.prospecto.runtime.accessor;

/**
 * A factory that produces {@link MapAccessor} objects.
 *
 * @author Carl Harris
 */
public interface MapAccessorFactory {

  MapAccessor newAccessor(Accessor accessor);

}
