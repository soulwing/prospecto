package org.soulwing.prospecto.api.template;

import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;

/**
 * A view node representing a map.
 *
 * @author Carl Harris
 */
public interface MapNode extends UpdatableNode {

  /**
   * Gets the data type of the elements of this node.
   * @return component data type
   */
  Class<?> getComponentType();

  /**
   * Gets an iterator for the map entries associated with this node in the
   * given model.
   * @param model model containing the map to iterate
   * @return iterator for the subject entries
   * @throws Exception
   */
  Iterator<Map.Entry<?, ?>> iterator(Object model) throws Exception;

  /**
   * Gets this node's default association manager.
   * @return association manager
   */
  ToManyMappedAssociationManager<?, ?, ?> getDefaultManager();

}
