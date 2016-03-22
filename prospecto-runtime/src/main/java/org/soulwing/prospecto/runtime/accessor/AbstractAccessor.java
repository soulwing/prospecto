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

import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;

/**
 * An abstract base for {@link Accessor} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractAccessor implements Accessor {

  private final EnumSet<AccessMode> accessModes;

  protected AbstractAccessor(EnumSet<AccessMode> accessModes) {
    this.accessModes = accessModes;
  }

  @Override
  public boolean canWrite() {
    return accessModes.contains(AccessMode.WRITE);
  }

  @Override
  public boolean canRead() {
    return accessModes.contains(AccessMode.READ);
  }

  @Override
  public EnumSet<AccessMode> getAccessModes() {
    return accessModes;
  }

}
