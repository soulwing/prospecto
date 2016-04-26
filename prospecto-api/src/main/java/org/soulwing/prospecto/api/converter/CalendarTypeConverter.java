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
package org.soulwing.prospecto.api.converter;

import java.util.Calendar;
import java.util.TimeZone;

import org.soulwing.prospecto.api.ViewContext;

/**
 * A {@link ValueTypeConverter} that converts {@link Calendar} objects to and
 * from a string representation.
 *
 * @author Carl Harris
 */
public class CalendarTypeConverter implements ValueTypeConverter {

  private final DateTypeConverter delegate = new DateTypeConverter();

  /**
   * Constructs a new instance that uses ISO 8601 format.
   */
  public CalendarTypeConverter() {
    delegate.setFormat(DateTypeConverter.Format.ISO8601);
  }

  /**
   * Constructs an instance that uses a custom pattern.
   * @param pattern custom pattern
   */
  public CalendarTypeConverter(String pattern) {
    delegate.setFormat(DateTypeConverter.Format.CUSTOM);
    delegate.setPattern(pattern);
  }

  /**
   * Constructs an instance that uses a specific format.
   * @param format format
   */
  public CalendarTypeConverter(DateTypeConverter.Format format) {
    delegate.setFormat(format);
  }

  @Override
  public boolean supports(Class<?> type) {
    return Calendar.class.isAssignableFrom(type);
  }

  @Override
  public Class<String> getType() {
    return delegate.getType();
  }

  @Override
  public Object toViewValue(Object modelValue, ViewContext context) throws Exception {
    assert modelValue instanceof Calendar;
    return delegate.toViewValue(((Calendar) modelValue).getTime(), context);
  }

  @Override
  public Calendar toModelValue(Object viewValue, ViewContext context) throws Exception {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(delegate.toModelValue(viewValue, context));
    return calendar;
  }

  /**
   * Gets the {@code format} property.
   * @return property value
   */
  public DateTypeConverter.Format getFormat() {
    return delegate.getFormat();
  }

  /**
   * Sets the {@code format} property.
   * @param format the property value to set
   */
  public void setFormat(DateTypeConverter.Format format) {
    delegate.setFormat(format);
  }

  /**
   * Gets the {@code pattern} property.
   * @return property value
   */
  public String getPattern() {
    return delegate.getPattern();
  }

  /**
   * Sets the {@code pattern} property.
   * @param pattern the property value to set
   */
  public void setPattern(String pattern) {
    delegate.setPattern(pattern);
  }

  /**
   * Gets the {@code timeZone} property.
   * @return property value
   */
  public TimeZone getTimeZone() {
    return delegate.getTimeZone();
  }

  /**
   * Sets the {@code timeZone} property.
   * @param timeZone the property value to set
   */
  public void setTimeZone(TimeZone timeZone) {
    delegate.setTimeZone(timeZone);
  }

  /**
   * Gets the ID property of the selected time zone.
   * @return time zone ID
   * @see TimeZone#getID()
   */
  public String getTimeZoneId() {
    return delegate.getTimeZoneId();
  }

  /**
   * Sets the {@code timeZone} property to the zone that corresponds to the
   * given ID.
   * @param id identifier of the time zone to set
   * @see TimeZone#getTimeZone(String)
   */
  public void setTimeZoneId(String id) {
    delegate.setTimeZoneId(id);
  }

  /**
   * A builder that produces a {@link CalendarTypeConverter}.
   */
  public static class Builder {

    private final CalendarTypeConverter converter = new CalendarTypeConverter();

    /**
     * Constructs a new instance.
     * @return builder
     */
    public static Builder with() {
      return new Builder();
    }

    private Builder() {
    }

    /**
     * Configures the {@code format} property.
     * @param format the property value to set
     * @return this builder
     */
    public Builder format(DateTypeConverter.Format format) {
      converter.setFormat(format);
      return this;
    }

    /**
     * Configures the {@code pattern} property.
     * @param pattern the property value to set
     * @return this builder
     */
    public Builder pattern(String pattern) {
      converter.setPattern(pattern);
      return this;
    }

    /**
     * Configures the {@code timeZone} property.
     * @param timeZone the property value to set
     * @return this builder
     */
    public Builder timeZone(TimeZone timeZone) {
      converter.setTimeZone(timeZone);
      return this;
    }

    /**
     * Configures the {@code timeZoneId} property.
     * @param timeZoneId the property value to set
     * @return this builder
     */
    public Builder timeZoneId(String timeZoneId) {
      converter.setTimeZoneId(timeZoneId);
      return this;
    }

    /**
     * Builds the converter.
     * @return converter
     */
    public CalendarTypeConverter build() {
      return converter;
    }

  }

}
