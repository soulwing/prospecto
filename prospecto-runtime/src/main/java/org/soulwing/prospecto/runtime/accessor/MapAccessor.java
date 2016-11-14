package org.soulwing.prospecto.runtime.accessor;

import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;

/**
 * TODO: DESCRIBE THE TYPE HERE
 *
 * @author Carl Harris
 */
public interface MapAccessor
    extends ToManyMappedAssociationManager<Object, Object, Object> {

  EnumSet<AccessMode> getSupportedModes();

}
