/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.jaxrs.runtime;

import org.soulwing.prospecto.api.url.UnresolvedUrlException;
import org.soulwing.prospecto.jaxrs.api.ModelPathSpec;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;

/**
 * An exception thrown to indicate that the path for a referenced resource 
 * could not be resolved because the resource was not found.
 *
 * @author Carl Harris
 */
public class ResourceNotFoundException extends UnresolvedUrlException {

  private static final long serialVersionUID = 3381534707381732991L;

  public ResourceNotFoundException(ModelPath modelPath) {
    super("cannot resolve a resource referenced by model types "
        + modelPath
        + "; perhaps you need to apply the @"
        + ModelPathSpec.class.getSimpleName()
        + " annotation to desired resource"); 
  }

}
