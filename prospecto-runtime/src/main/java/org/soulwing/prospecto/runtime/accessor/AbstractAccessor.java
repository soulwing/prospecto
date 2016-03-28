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

/**
 * An abstract base for {@link Accessor} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractAccessor implements Accessor {

  private final String name;
  private final AccessType accessType;
  private final EnumSet<AccessMode> supportedAccessModes;

  private EnumSet<AccessMode> effectiveAccessModes =
      EnumSet.allOf(AccessMode.class);

  protected AbstractAccessor(String name, AccessType accessType,
      EnumSet<AccessMode> supportedAccessModes) {
    this.name = name;
    this.accessType = accessType;
    this.supportedAccessModes = supportedAccessModes;
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
  public boolean canRead() {
    return supportedAccessModes.contains(AccessMode.READ)
        && effectiveAccessModes.contains(AccessMode.READ);
  }

  @Override
  public boolean canWrite() {
    return supportedAccessModes.contains(AccessMode.WRITE)
        && effectiveAccessModes.contains(AccessMode.WRITE);
  }

  @Override
  public EnumSet<AccessMode> getAccessModes() {
    final EnumSet<AccessMode> accessModes = EnumSet.copyOf(supportedAccessModes);
    accessModes.retainAll(effectiveAccessModes);
    return accessModes;
  }

  @Override
  public void setAccessModes(EnumSet<AccessMode> accessModes) {
    effectiveAccessModes = accessModes;
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

}
