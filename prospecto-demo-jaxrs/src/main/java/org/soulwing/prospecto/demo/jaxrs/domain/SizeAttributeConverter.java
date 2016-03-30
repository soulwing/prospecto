/*
 * File created on Mar 29, 2016
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
package org.soulwing.prospecto.demo.jaxrs.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * An {@link AttributeConverter} for a {@link Size}.
 *
 * @author Carl Harris
 */
@Converter(autoApply = true)
public class SizeAttributeConverter
    implements AttributeConverter<Size, String> {

  @Override
  public String convertToDatabaseColumn(Size size) {
    if (size == null) return null;
    return size.toString();
  }

  @Override
  public Size convertToEntityAttribute(String s) {
    if (s == null || s.isEmpty()) return null;
    return Size.valueOf(s);
  }

}
