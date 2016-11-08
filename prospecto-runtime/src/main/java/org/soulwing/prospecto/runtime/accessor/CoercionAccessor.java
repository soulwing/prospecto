package org.soulwing.prospecto.runtime.accessor;

import java.lang.reflect.Array;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * An accessor that invokes array coercion methods on the object returned
 * by a delegate accessor.
 *
 * @author Carl Harris
 */
class CoercionAccessor implements Accessor {


  private final ArrayCoercion coercion;
  private final Accessor delegate;
  private final Class<?> dataType;

  CoercionAccessor(Accessor delegate, Class<?> componentType)
      throws NoSuchMethodException {
    this.delegate = delegate;
    this.dataType = Array.newInstance(componentType, 0).getClass();
    this.coercion = ArrayCoercion.newInstance(delegate.getDataType(), dataType);
  }

  @Override
  public Class<?> getDataType() {
    return dataType;
  }

  @Override
  public Accessor forSubtype(Class<?> subtype) throws Exception {
    return delegate.forSubtype(subtype);
  }

  @Override
  public Object get(Object source) throws Exception {
    return coercion.toArray(delegate.get(source));
  }

  @Override
  public void set(Object target, Object value) throws Exception {
    delegate.set(target, coercion.fromArray((Object[]) value));
  }

  @Override
  public Class<?> getModelType() {
    return delegate.getModelType();
  }

  @Override
  public String getName() {
    return delegate.getName();
  }

  @Override
  public AccessType getAccessType() {
    return delegate.getAccessType();
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return delegate.getSupportedModes();
  }

  @Override
  public boolean canRead() {
    return delegate.canRead();
  }

  @Override
  public boolean canWrite() {
    return delegate.canWrite();
  }

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return delegate.supports(descriptor);
  }

  @Override
  public boolean isSameAssociate(Object owner, ViewEntity associateEntity,
      ObjectFactory objectFactory) throws Exception {
    return delegate.isSameAssociate(owner, associateEntity, objectFactory);
  }

  @Override
  public Object newAssociate(Object owner, ViewEntity associateEntity,
      ObjectFactory objectFactory) throws Exception {
    return delegate.newAssociate(owner, associateEntity, objectFactory);
  }
}
