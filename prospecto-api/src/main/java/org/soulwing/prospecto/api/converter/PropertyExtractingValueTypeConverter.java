/*
 * File created on Mar 13, 2016
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
package org.soulwing.prospecto.api.converter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * A converter that extracts a single String, Number, Boolean, Enum, or
 * Date property from a model object.
 *
 * @author Carl Harris
 */
public class PropertyExtractingValueTypeConverter
    implements ValueTypeConverter<Object> {

  private Class<?> modelType;
  private boolean supportSubTypes = true;
  private String propertyName;
  private Method getter;

  @PostConstruct
  public void init() throws Exception {
    assertNotNull(modelType, "modelType is required");
    assertNotNull(propertyName, "propertyName is required");
    getter = findGetter(modelType, propertyName);
    assertValidValueType(propertyName, getter.getReturnType());
  }

  private static Method findGetter(Class<?> modelType, String propertyName)
      throws IntrospectionException {
    for (final PropertyDescriptor descriptor :
        Introspector.getBeanInfo(modelType).getPropertyDescriptors()) {
      if (descriptor.getName().equals(propertyName)) {
        return descriptor.getReadMethod();
      }
    }
    throw new IllegalArgumentException(modelType.getName()
        + " has no property named '" + propertyName + "'");
  }

  private static void assertNotNull(Object obj, String message) {
    if (obj != null) return;
    throw new IllegalArgumentException(message);
  }

  private static void assertValidValueType(String propertyName,
      Class<?> valueType) {
    if (String.class.equals(valueType)) return;
    if (Number.class.isAssignableFrom(valueType)) return;
    if (Boolean.class.equals(valueType)) return;
    if (Enum.class.isAssignableFrom(valueType)) return;
    if (Date.class.isAssignableFrom(valueType)) return;
    if (Calendar.class.isAssignableFrom(valueType)) return;
    throw new IllegalArgumentException("property '" + propertyName
        + "' must be of type String, Number, Boolean, Enum, or Date; not "
        + valueType.getName());
  }

  @Override
  public boolean supports(Class<?> type) {
    return supportSubTypes ?
        modelType.isAssignableFrom(type) : modelType.equals(type);
  }

  @Override
  public Class<?> getViewType() {
    return getter.getReturnType();
  }

  @Override
  public Object toValue(Object model) throws Exception {
    return getter.invoke(model);
  }

  @Override
  public Object toObject(Object value) throws Exception {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the {@code modelType} property.
   * @return property value
   */
  public Class<?> getModelType() {
    return modelType;
  }

  /**
   * Sets the {@code modelType} property.
   * @param modelType the property value to set
   */
  public void setModelType(Class<?> modelType) {
    this.modelType = modelType;
  }

  /**
   * Gets the {@code supportSubTypes} property.
   * @return property value
   */
  public boolean isSupportSubTypes() {
    return supportSubTypes;
  }

  /**
   * Sets the {@code supportSubTypes} property.
   * @param supportSubTypes the property value to set
   */
  public void setSupportSubTypes(boolean supportSubTypes) {
    this.supportSubTypes = supportSubTypes;
  }

  /**
   * Gets the {@code propertyName} property.
   * @return property value
   */
  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Sets the {@code propertyName} property.
   * @param propertyName the property value to set
   */
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * A builder that produces a {@link PropertyExtractingValueTypeConverter}.
   * @author Carl Harris
   */
  public static class Builder {

    private final PropertyExtractingValueTypeConverter converter =
        new PropertyExtractingValueTypeConverter();

    /**
     * Creates a new builder instance.
     * @return builder
     */
    public static Builder with() {
      return new Builder();
    }

    private Builder() {
    }

    /**
     * Configures the {@code modelType} property.
     * @param modelType the property value to set
     * @return this builder
     */
    public Builder modelType(Class<?> modelType) {
      converter.setModelType(modelType);
      return this;
    }

    /**
     * Configures the {@code propertyName} property.
     * @param propertyName the property value to set
     * @return this builder
     */
    public Builder propertyName(String propertyName) {
      converter.setPropertyName(propertyName);
      return this;
    }

    /**
     * Configures the {@code supportSubTypes} property.
     * @param supportSubTypes the property value to set
     * @return this builder
     */
    public Builder supportSubTypes(boolean supportSubTypes) {
      converter.setSupportSubTypes(supportSubTypes);
      return this;
    }

    public PropertyExtractingValueTypeConverter build() {
      try {
        converter.init();
        return converter;
      }
      catch (Exception ex) {
        throw new ViewTemplateException(ex);
      }
    }

  }

}
