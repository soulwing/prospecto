/*
 * File created on Apr 4, 2016
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
package org.soulwing.prospecto.api.discriminator;

/**
 * A discriminator that uses the unqualified class name and takes one level
 * of class nesting into account.
 *
 * @author Carl Harris
 */
public class UnqualifiedClassNameDiscriminatorStrategy
    implements DiscriminatorStrategy {

  @Override
  public Discriminator toDiscriminator(Class<?> base, Class<?> subtype) {
    final StringBuilder sb = new StringBuilder();

    final String className = subtype.getName();
    final String packageName = subtype.getPackage().getName();
    return new Discriminator(className.substring(packageName.length() + 1));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Class<T> toSubtype(Class<T> base, Discriminator discriminator)
      throws ClassNotFoundException {
    final StringBuilder sb = new StringBuilder();
    sb.append(base.getPackage().getName());
    sb.append('.');
    sb.append(discriminator.getValue());
    final String name = sb.toString();
    final Class<?> subtype = base.getClassLoader().loadClass(name);
    if (!(base.isAssignableFrom(subtype))) {
      throw new IllegalArgumentException(
          name + " is not a subtype of " + base.getName());
    }
    return (Class<T>) subtype;
  }

}
