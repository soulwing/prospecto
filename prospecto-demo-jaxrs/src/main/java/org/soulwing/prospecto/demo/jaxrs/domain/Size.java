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
 * A value type that represents a size (e.g. for a jersey).
 *
 * @author Carl Harris
 */
public final class Size implements Serializable, Cloneable, Comparable<Size> {

  /**
   * An enumeration of size ranges.
   */
  public enum Range {
    YOUTH,
    ADULT
  }

  /**
   * An enumeration of size levels.
   */
  public enum Level {
    SMALL,
    MEDIUM,
    LARGE,
    X_LARGE,
    XX_LARGE
  }

  static final char DELIMITER = '/';

  private final Range range;
  private final Level level;

  private Size(Range range, Level level) {
    this.range = range;
    this.level = level;
  }

  @Override
  public int hashCode() {
    return Objects.hash(range, level);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Size)) return false;
    Size that = (Size) obj;
    return Objects.equals(this.range, that.range)
        && Objects.equals(this.level, that.level);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public int compareTo(Size other) {
    int result = this.range.ordinal() - other.range.ordinal();
    if (result != 0) return result;
    return this.level.ordinal() - other.level.ordinal();
  }

  @Override
  public String toString() {
    return range.name() + DELIMITER + level.name();
  }

  /**
   * Constructs a new instance using the given range and level.
   * @param range size range
   * @param level size level
   * @return size instance
   */
  public static Size of(Range range, Level level) {
    if (range == null || level == null) {
      throw new NullPointerException("range and level are required");
    }
    return new Size(range, level);
  }

  /**
   * Constructs a new instance using the given string representation.
   * @param value string representation of size
   * @return size instance
   * @throws NullPointerException if {@code value} is {@code null}
   * @throws IllegalArgumentException if {@code value} is not a valid
   *    string representation of a size
   */
  public static Size valueOf(String value) {
    if (value == null) {
      throw new NullPointerException("value is required");
    }

    value = value.trim();
    final int index = value.indexOf(DELIMITER);
    if (index == -1) {
      throw new IllegalArgumentException("no delimiter found");
    }

    final Range range = Range.valueOf(value.substring(0, index));
    final Level level = Level.valueOf(value.substring(index + 1));
    return new Size(range, level);
  }

}
