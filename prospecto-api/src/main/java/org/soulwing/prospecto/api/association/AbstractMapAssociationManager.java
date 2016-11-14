package org.soulwing.prospecto.api.association;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An abstract association manager for a {@link Map}.
 *
 * @author Carl Harris
 */
public abstract class AbstractMapAssociationManager<T, K, E>
    extends AbstractAssociationManager<T, E>
    implements ToManyMappedAssociationManager<T, K, E> {

  @Override
  public Iterator<Map.Entry<K,E>> iterator(T owner) throws Exception {
    final Map<K, E> associates = getAssociates(owner);
    if (associates == null) return null;
    return associates.entrySet().iterator();
  }

  @Override
  public int size(T owner) throws Exception {
    final Map<K, E> associates = getAssociates(owner);
    if (associates == null) return 0;
    return associates.size();
  }

  @Override
  public E get(T owner, K key) throws Exception {
    return getOrInitAssociates(owner).get(key);
  }

  @Override
  public void put(T owner, K key, E associate) throws Exception {
    getOrInitAssociates(owner).put(key, associate);
  }

  @Override
  public void remove(T owner, K key) throws Exception {
    getOrInitAssociates(owner).remove(key);
  }

  @Override
  public void clear(T owner) throws Exception {
    getOrInitAssociates(owner).clear();
  }

  /**
   * Begins a transaction for updating the associated collection.
   * <p>
   * This implementation does nothing. Subclasses should override as needed.
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void begin(T owner) throws Exception {}

  /**
   * Ends a transaction that has updated the associated collection.
   * <p>
   * This implementation does nothing. Subclasses should override as needed.
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void end(T owner) throws Exception {}

  private Map<K, E> getOrInitAssociates(T owner) throws Exception {
    Map<K, E> associates = getAssociates(owner);
    if (associates == null) {
      associates = newAssociates();
      setAssociates(owner, associates);
    }
    return associates;
  }


  /**
   * Gets the map of associates.
   * @param owner association owner
   * @return map
   * @throws Exception
   */
  protected abstract Map<K, E> getAssociates(T owner) throws Exception;

  /**
   * Sets the map of associates.
   * <p>
   * This method is called when a manager method is called to modify the
   * list and {@link #getAssociates(Object)} returns {@code null}. The
   * default implementation throws {@link UnsupportedOperationException}.
   * <p>
   * If your implementation of {@link #getAssociates(Object)} can return
   * {@code null}, you must override this method to allow the map to
   * be initialized.
   *
   * @param owner the association owner
   * @throws Exception
   */
  protected void setAssociates(T owner, Map<K, E> associates)
      throws Exception {
    throw new UnsupportedOperationException(
        "manager does not implement setAssociates");
  }

  /**
   * Creates a new (empty) associates map.
   * <p>
   * The default implementation returns a {@link HashMap}. Override if
   * you wish to use a different {@link Map} subtype.
   *
   * @return map of associates (never {@code null})
   */
  protected Map<K, E> newAssociates() {
    return new HashMap<>();
  }


}
