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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A ordered collection of tokens.
 *
 * @author Carl Harris
 */
public final class TokenList implements Serializable, Cloneable {

  private final List<Token> tokens;

  private TokenList(List<Token> tokens) {
    this.tokens = tokens;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokens);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof TokenList)) return false;
    return Objects.equals(this.tokens, ((TokenList) obj).tokens);
  }

  @Override
  public TokenList clone() {
    try {
      return (TokenList) super.clone();
    }
    catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Gets a printable representation of the list, in which the tokens are
   * printed in order, with a single space character between each pair of
   * adjacent tokens.
   * @return token list string
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    final Iterator<Token> i = tokens.iterator();
    while (i.hasNext()) {
      sb.append(i.next());
      if (i.hasNext()) {
        sb.append(' ');
      }
    }
    return sb.toString();
  }

  /**
   * Converts this token list to a {@link List}.
   * <p>
   * Manipulating the returned list (e.g. adding/removing elements) has no
   * effect on this token list.
   *
   * @return list of tokens contained in this token list
   */
  public List<Token> toList() {
    return clone().tokens;
  }

  /**
   * Constructs a new instance from a string.
   * @param value the subject string
   * @return token list consisting of the tokens contained in string
   */
  public static TokenList valueOf(String value) {
    if (value == null) {
      throw new NullPointerException("value is required");
    }

    final String[] values = value.split("\\s+");
    final List<Token> tokens = new ArrayList<>(values.length);
    for (final String s : values) {
      tokens.add(Token.valueOf(s));
    }

    return new TokenList(tokens);
  }

  /**
   * Constructs an instance containing no tokens.
   * @return empty token list
   */
  public static TokenList empty() {
    return new TokenList(Collections.<Token>emptyList());
  }

}
