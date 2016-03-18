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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.junit.Test;

/**
 * Unit tests for {@link DateTypeConverter}.
 *
 * @author Carl Harris
 */
public class DateTypeConverterTest {

  private DateTypeConverter converter = new DateTypeConverter();

  @Test
  public void testConvertUsingDefaults() throws Exception {
    converter.setTimeZoneId("GMT");
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("1970-01-01T00:00:00")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingISO8601() throws Exception {
    converter.setTimeZoneId("GMT");
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("1970-01-01T00:00:00")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingISO8601WithTimeZone() throws Exception {
    converter.setTimeZoneId("GMT");
    converter.setFormat(DateTypeConverter.Format.ISO8601_WITH_TIME_ZONE);
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("1970-01-01T00:00:00Z")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingISO8601Date() throws Exception {
    converter.setTimeZoneId("GMT");
    converter.setFormat(DateTypeConverter.Format.ISO8601_DATE);
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("1970-01-01")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingISO8601Time() throws Exception {
    converter.setTimeZoneId("GMT");
    converter.setFormat(DateTypeConverter.Format.ISO8601_TIME);
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("00:00:00")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingRFC1123() throws Exception {
    converter.setTimeZoneId("GMT");
    converter.setFormat(DateTypeConverter.Format.RFC1123);
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("Thu, 01 Jan 1970 00:00:00 GMT")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingEpoch() throws Exception {
    Long timeStamp = 1451649600000L;
    converter.setFormat(DateTypeConverter.Format.EPOCH);
    final Date date = new Date(timeStamp);
    Long value = (Long) converter.toValue(date);
    assertThat(value, is(equalTo(timeStamp)));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testConvertUsingCustomPattern() throws Exception {
    converter.setTimeZoneId("GMT");
    converter.setFormat(DateTypeConverter.Format.CUSTOM);
    converter.setPattern("MM/dd/yyy HH:mm:ss");
    final Date date = new Date(0);
    final String value = (String) converter.toValue(date);
    assertThat(value, is(equalTo("01/01/1970 00:00:00")));
    assertThat(converter.toObject(value), is(equalTo(date)));
  }

  @Test
  public void testSupports() throws Exception {
    converter.setSupportedType(java.sql.Time.class);
    assertThat(converter.supports(java.sql.Timestamp.class), is(false));
    assertThat(converter.supports(java.sql.Date.class), is(false));
    assertThat(converter.supports(Date.class), is(false));
  }

}
