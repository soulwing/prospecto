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

import java.io.Serializable;
import java.util.Objects;

/**
 * A value type representing a string of one or more non-whitespace characters.
 *
 * @author Carl Harris
 */
public final class Token implements Serializable, Cloneable, Comparable<Token> {

  private String value;

  private Token(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Token)) return false;
    return Objects.equals(this.value, ((Token) obj).value);
  }

  @Override
  public Token clone() {
    try {
      return (Token) super.clone();
    }
    catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public int compareTo(Token other) {
    return this.value.compareTo(other.value);
  }

  @Override
  public String toString() {
    return value;
  }

  /**
   * Create a new instance from the given string.
   * @param value non-empty string value
   * @return token instance
   * @throws NullPointerException if {@code value is null}
   * @throws IllegalArgumentException if token is empty or contains whitespace
   */
  public static Token valueOf(String value) {
    if (value == null) {
      throw new NullPointerException("value is required");
    }

    value = value.trim();
    if (value.isEmpty()) {
      throw new IllegalArgumentException("empty value is not allowed");
    }

    for (final char c : value.toCharArray()) {
      if (Character.isWhitespace(c)) {
        throw new IllegalArgumentException("value must not contain whitespace");
      }
    }

    return new Token(value);
  }

}
