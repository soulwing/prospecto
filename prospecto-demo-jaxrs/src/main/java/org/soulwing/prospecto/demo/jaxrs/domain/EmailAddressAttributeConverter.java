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
 * An {@link AttributeConverter} for an {@link EmailAddress}.
 *
 * @author Carl Harris
 */
@Converter(autoApply = true)
public class EmailAddressAttributeConverter
    implements AttributeConverter<EmailAddress, String> {

  @Override
  public String convertToDatabaseColumn(EmailAddress emailAddress) {
    if (emailAddress == null) return null;
    return emailAddress.toString();
  }

  @Override
  public EmailAddress convertToEntityAttribute(String s) {
    if (s == null || s.isEmpty()) return null;
    return EmailAddress.valueOf(s);
  }

}
