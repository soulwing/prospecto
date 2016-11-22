package org.soulwing.prospecto.runtime.association;

import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A {@link ToManyMappedAssociationUpdater} that supports the map-of-references
 * structure.
 *
 * @author Carl Harris
 */
public class ReferenceMapToManyMappedAssociationUpdater
    extends ValueMapToManyMappedAssociationUpdater {

  public static final ReferenceMapToManyMappedAssociationUpdater INSTANCE =
      new ReferenceMapToManyMappedAssociationUpdater();

  private ReferenceMapToManyMappedAssociationUpdater() {
  }

  @Override
  protected Object resolve(Object value,
      ScopedViewContext context) {
    final InjectableViewEntity entity = (InjectableViewEntity) value;
    return context.getReferenceResolvers().resolve(entity.getType(), entity);
  }

}
