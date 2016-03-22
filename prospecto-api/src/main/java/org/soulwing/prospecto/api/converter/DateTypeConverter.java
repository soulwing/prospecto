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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

/**
 * A {@link ValueTypeConverter} that converts {@link Date} objects to and from
 * a string representation.
 * <p>
 * Note that by default this converter claims support for all {@link Date}
 * subtypes (including the SQL subtypes {@link java.sql.Date},
 * {@link java.sql.Time}, and {@link java.sql.Timestamp}). If you wish to use
 * different conversions for different subtypes, create multiple instances
 * each configured for a different subtype, and set the {@code supportSubTypes}
 * property to {@code false}.
 *
 * {@link org.soulwing.prospecto.api.ViewContext}.
 *
 * @author Carl Harris
 */
public class DateTypeConverter implements ValueTypeConverter<String> {

  public enum Format {
    ISO8601,
    ISO8601_DATE,
    ISO8601_TIME,
    ISO8601_WITH_TIME_ZONE,
    RFC1123,
    CUSTOM
  }

  private static String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

  private static String ISO8601_DATE_PATTERN = "yyyy-MM-dd";

  private static String ISO8601_TIME_PATTERN = "HH:mm:ss";

  private static String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

  private Class<? extends Date> supportedType = Date.class;
  private boolean supportSubTypes = true;
  private Format format = Format.ISO8601;
  private String pattern = ISO8601_PATTERN;
  private TimeZone timeZone = TimeZone.getDefault();

  /**
   * Constructs a new instance that uses ISO 8601 format.
   */
  public DateTypeConverter() {
    this(Format.ISO8601);
  }

  /**
   * Constructs an instance that uses a custom pattern.
   * @param pattern custom pattern
   */
  public DateTypeConverter(String pattern) {
    this(Format.CUSTOM);
    this.pattern = pattern;
  }

  /**
   * Constructs an instance that uses a specific format.
   * @param format format
   */
  public DateTypeConverter(Format format) {
    this.format = format;
  }

  @Override
  public boolean supports(Class<?> type) {
    return supportSubTypes ?
        supportedType.isAssignableFrom(type) : supportedType.equals(type);
  }

  @Override
  public Class<String> getViewType() {
    return String.class;
  }

  @Override
  public String toValue(Object model) throws Exception {
    assert model instanceof Date;
    switch (format) {
      default:
      case ISO8601:
        return formatUsingPattern(ISO8601_PATTERN, (Date) model);
      case ISO8601_DATE:
        return formatUsingPattern(ISO8601_DATE_PATTERN, (Date) model);
      case ISO8601_TIME:
        return formatUsingPattern(ISO8601_TIME_PATTERN, (Date) model);
      case ISO8601_WITH_TIME_ZONE:
        return formatUsing8601WithTimeZone((Date) model);
      case RFC1123:
        return formatUsingPattern(RFC1123_PATTERN, (Date) model);
      case CUSTOM:
        return formatUsingPattern(
            pattern != null ? pattern : ISO8601_PATTERN, (Date) model);
    }
  }

  @Override
  public Date toObject(Object value) throws Exception {
    switch (format) {
      default:
      case ISO8601:
        return parseUsingPattern(ISO8601_PATTERN, value.toString());
      case ISO8601_DATE:
        return parseUsingPattern(ISO8601_DATE_PATTERN, value.toString());
      case ISO8601_TIME:
        return parseUsingPattern(ISO8601_TIME_PATTERN, value.toString());
      case ISO8601_WITH_TIME_ZONE:
        return parseUsing8601WithTimeZone(value.toString());
      case RFC1123:
        return parseUsingPattern(RFC1123_PATTERN, value.toString());
      case CUSTOM:
        return parseUsingPattern(
            pattern != null ? pattern : ISO8601_PATTERN, value.toString());
    }
  }

  private String formatUsingPattern(String pattern, Date date) {
    final DateFormat df = new SimpleDateFormat(pattern);
    df.setTimeZone(timeZone);
    return df.format(date);
  }

  private Date parseUsingPattern(String pattern, String text)
      throws ParseException {
    final DateFormat df = new SimpleDateFormat(pattern);
    df.setTimeZone(timeZone);
    return df.parse(text);
  }

  private String formatUsing8601WithTimeZone(Date date) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.setTimeZone(timeZone);
    return DatatypeConverter.printDateTime(calendar);
  }

  private Date parseUsing8601WithTimeZone(String text) {
    return DatatypeConverter.parseDateTime(text).getTime();
  }

  /**
   * Gets the {@code supportedType} property.
   * @return property value
   */
  public Class<? extends Date> getSupportedType() {
    return supportedType;
  }

  /**
   * Sets the {@code supportedType} property.
   * @param supportedType the property value to set
   */
  public void setSupportedType(Class<? extends Date> supportedType) {
    if (supportedType == null) {
      throw new NullPointerException("supported type must not be null");
    }
    if (!Date.class.isAssignableFrom(supportedType)) {
      throw new IllegalArgumentException("supported type must be a subtype of "
          + Date.class.getName() + " not " + supportedType);
    }
    this.supportedType = supportedType;
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
   * Gets the {@code format} property.
   * @return property value
   */
  public Format getFormat() {
    return format;
  }

  /**
   * Sets the {@code format} property.
   * @param format the property value to set
   */
  public void setFormat(Format format) {
    this.format = format;
  }

  /**
   * Gets the {@code pattern} property.
   * @return property value
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Sets the {@code pattern} property.
   * @param pattern the property value to set
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Gets the {@code timeZone} property.
   * @return property value
   */
  public TimeZone getTimeZone() {
    return timeZone;
  }

  /**
   * Sets the {@code timeZone} property.
   * @param timeZone the property value to set
   */
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * Gets the ID property of the selected time zone.
   * @return time zone ID
   * @see TimeZone#getID()
   */
  public String getTimeZoneId() {
    return getTimeZone().getID();
  }

  /**
   * Sets the {@code timeZone} property to the zone that corresponds to the
   * given ID.
   * @param id identifier of the time zone to set
   * @see TimeZone#getTimeZone(String)
   */
  public void setTimeZoneId(String id) {
    setTimeZone(TimeZone.getTimeZone(id));
  }

  /**
   * A builder that produces a {@link DateTypeConverter}.
   */
  public static class Builder {

    private final DateTypeConverter converter = new DateTypeConverter();

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
     * Configures the {@code supportedType} property.
     * @param supportedType the property value to set
     * @return this builder
     */
    public Builder supportedType(Class<? extends Date> supportedType) {
      converter.setSupportedType(supportedType);
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

    /**
     * Configures the {@code format} property.
     * @param format the property value to set
     * @return this builder
     */
    public Builder format(Format format) {
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
    public DateTypeConverter build() {
      return converter;
    }

  }

}
