package org.soulwing.prospecto.runtime.accessor;

import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;

/**
 * An accessor for a {@link java.util.Map}.
 *
 * @author Carl Harris
 */
public interface MapAccessor
    extends ToManyMappedAssociationManager<Object, Object, Object> {

  EnumSet<AccessMode> getSupportedModes();

}
