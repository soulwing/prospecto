/*
 * File created on Mar 16, 2016
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

import java.beans.Introspector;

import org.soulwing.prospecto.api.options.ViewDefaults;

/**
 * A {@link DiscriminatorStrategy} that utilizes the simple (unqualified)
 * class name.
 * <p>
 * This strategy provides options for stripping a prefix and/or suffix from
 * the simple class name and for de-capitalizing the (stripped) name
 * (according to the unusual rules of {@link Introspector#decapitalize(String)}.
 * <p>
 * In oder for this strategy to successfully recover the {@link Class} object
 * for a subtype from the simple names produced by {@link #toDiscriminator(Class, Class)},
 * the subtype classes must be in the same package as the base type and loaded
 * by the same class loader as the base type.
 *
 * @author Carl Harris
 */
public class SimpleClassNameDiscriminatorStrategy implements DiscriminatorStrategy {

  private String name = ViewDefaults.DISCRIMINATOR_NAME;
  private String prefix;
  private String suffix;
  private boolean decapitalize;

  @Override
  public Discriminator toDiscriminator(Class<?> base, Class<?> subtype) {
    if (!base.isAssignableFrom(subtype)) {
      throw new IllegalArgumentException(subtype + " is not a subtype of "
          + base);
    }
    String value = subtype.getSimpleName();
    if (prefix != null && value.startsWith(prefix)) {
      value = value.substring(prefix.length());
    }
    if (suffix != null && value.endsWith(suffix)) {
      value = value.substring(0, value.length() - suffix.length());
    }
    if (decapitalize) {
      value = Introspector.decapitalize(value);
    }
    return new Discriminator(value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Class<T> toSubtype(Class<T> base, Discriminator discriminator)
      throws ClassNotFoundException {
    String simpleClassName = discriminator.getValue().toString();
    if (simpleClassName.isEmpty()) {
      throw new IllegalArgumentException("discriminator value is empty");
    }
    if (decapitalize) {
      simpleClassName = recapitalize(simpleClassName);
    }
    if (prefix != null) {
      simpleClassName = prefix + simpleClassName;
    }
    if (suffix != null) {
      simpleClassName = simpleClassName + suffix;
    }
    final String name = base.getPackage().getName() + "." + simpleClassName;
    final Class<?> subtype = base.getClassLoader().loadClass(name);
    if (!(base.isAssignableFrom(subtype))) {
      throw new IllegalArgumentException(
          name + " is not a subtype of " + base.getName());
    }
    return (Class<T>) subtype;
  }

  private String recapitalize(String name) {
    final char c = name.charAt(0);
    if (Character.isLowerCase(c)) {
      name = Character.toUpperCase(c) + name.substring(1);
    }
    return name;
  }

  /**
   * Gets the {@code name} property.
   * @return property value
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the {@code name} property.
   * @param name the property value to set
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Gets the {@code prefix} property.
   * @return property value
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Sets the {@code prefix} property.
   * @param prefix the property value to set
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * Gets the {@code suffix} property.
   * @return property value
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Sets the {@code suffix} property.
   * @param suffix the property value to set
   */
  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * Gets the {@code decapitalize} property.
   * @return property value
   */
  public boolean isDecapitalize() {
    return decapitalize;
  }

  /**
   * Sets the {@code decapitalize} property.
   * @param decapitalize the property value to set
   */
  public void setDecapitalize(boolean decapitalize) {
    this.decapitalize = decapitalize;
  }

  /**
   * A builder that produces a {@link SimpleClassNameDiscriminatorStrategy} instance.
   * @author Carl Harris
   */
  public static class Builder {

    private final SimpleClassNameDiscriminatorStrategy discriminator =
        new SimpleClassNameDiscriminatorStrategy();

    public static Builder with() {
      return new Builder();
    }

    public Builder name(String name) {
      discriminator.setName(name);
      return this;
    }

    public Builder stripPrefix(String prefix) {
      discriminator.setPrefix(prefix);
      return this;
    }

    public Builder stripSuffix(String suffix) {
      discriminator.setSuffix(suffix);
      return this;
    }

    public Builder decapitalize() {
      discriminator.setDecapitalize(true);
      return this;
    }

    public SimpleClassNameDiscriminatorStrategy build() {
      return discriminator;
    }

  }

}
