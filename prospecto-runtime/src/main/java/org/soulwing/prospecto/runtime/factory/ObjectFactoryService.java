package org.soulwing.prospecto.runtime.factory;

import org.soulwing.prospecto.api.factory.ObjectFactories;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * A service that creates model object instances using a collection of
 * {@link org.soulwing.prospecto.api.factory.ObjectFactory} strategies.
 *
 * @author Carl Harris
 */
public interface ObjectFactoryService extends ObjectFactories, ObjectFactory {
}
