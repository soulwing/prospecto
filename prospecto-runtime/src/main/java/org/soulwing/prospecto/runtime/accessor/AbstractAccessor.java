/*
 * File created on Mar 21, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.prospecto.runtime.accessor;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.association.AbstractToOneAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;

/**
 * An abstract base for {@link Accessor} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractAccessor
    extends AbstractToOneAssociationManager<Object, Object>
    implements Accessor {

  private final Class<?> modelType;
  private final String name;
  private final AccessType accessType;
  private final EnumSet<AccessMode> supportedModes;

  AbstractAccessor(Class<?> modelType, String name,
      AccessType accessType, EnumSet<AccessMode> supportedModes) {
    this.modelType = modelType;
    this.name = name;
    this.accessType = accessType;
    this.supportedModes = supportedModes;
  }

  @Override
  public Class<?> getModelType() {
    return modelType;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public AccessType getAccessType() {
    return accessType;
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return supportedModes;
  }

  @Override
  public boolean canRead() {
    return supportedModes.contains(AccessMode.READ);
  }

  @Override
  public boolean canWrite() {
    return supportedModes.contains(AccessMode.WRITE);
  }

  @Override
  public Accessor forSubtype(Class<?> subtype) throws Exception {
    return newAccessor(subtype, name);
  }

  protected abstract Accessor newAccessor(Class<?> type, String name)
      throws Exception;


  @Override
  public Object get(Object source)
      throws IllegalAccessException, InvocationTargetException {
    if (!canRead()) return UndefinedValue.INSTANCE;
    return onGet(source);
  }

  protected abstract Object onGet(Object source) throws IllegalAccessException,
      InvocationTargetException;

  @Override
  public void set(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (!canWrite()) return;
    onSet(target, value);
  }

  protected abstract void onSet(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException;

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return getDataType().isAssignableFrom(descriptor.getAssociateType());
  }

}
