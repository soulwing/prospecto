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
package org.soulwing.prospecto.jaxrs.api;

import java.util.List;

import org.soulwing.prospecto.api.url.UnresolvedUrlException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;

/**
 * An exception thrown when more the model path specification of more than one
 * resource method matches a given model path specification.
 *
 * @author Carl Harris
 */
public class AmbiguousPathResolutionException
    extends UnresolvedUrlException {

  public AmbiguousPathResolutionException(ModelPath modelPath,
      List<ResourceDescriptor> methodDescriptors) {
    super(createMessage(modelPath, methodDescriptors));
  }

  private static String createMessage(ModelPath modelPath,
      List<ResourceDescriptor> methodDescriptors) {
    StringBuilder sb = new StringBuilder();
    sb.append(modelPath);
    sb.append(" matches multiple resource methods: ");
    sb.append(methodDescriptors);
    return sb.toString();
  }

}
