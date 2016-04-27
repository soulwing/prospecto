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
package org.soulwing.prospecto.jaxrs.runtime.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.soulwing.prospecto.jaxrs.api.ModelPathSpec;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModel;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModelSequence;

/**
 * An abstraction for a path of model classes.
 *
 * @author Carl Harris
 */
public class ModelPath {

  public enum MatchType {
    EXACT,
    ANY,
    ANY_SEQUENCE;
  }

  private final List<Class<?>> path = new ArrayList<>();

  private ModelPath(List<Class<?>> path) {
    this.path.addAll(path);
  }

  /**
   * Creates a new path object with the given sequence of model types.
   * @param path sequence of model types
   * @return model path
   */
  public static ModelPath with(List<Class<?>> path) {
    return new ModelPath(path);
  }

  /**
   * Creates a new path object with the given sequence of model types.
   * @param path sequence of model types
   * @return model path
   */
  public static ModelPath with(Class<?>... path) {
    return new ModelPath(Arrays.asList(path));
  }

  /**
   * Creates a new model path from this path concatenated with the given
   * sequence of model types.
   * @param path sequence of model types to concatenate with this path
   * @return model path
   */
  public ModelPath concat(List<Class<?>> path) {
    List<Class<?>> p = new ArrayList<>(this.path.size() + path.size());
    p.addAll(this.path);
    p.addAll(path);
    return new ModelPath(p);
  }

  /**
   * Creates a new model path from this path concatenated with the given
   * sequence of model types.
   * @param path sequence of model types to concatenate with this path
   * @return model path
   */
  public ModelPath concat(Class<?>... path) {
    return concat(Arrays.asList(path));
  }

  /**
   * Creates a new model path from this path concatenated with the sequence
   * of model types on the given {@link ModelPathSpec} annotation.
   * sequence of model types.
   * @param modelPathSpec a referenced by annotation (which may be {@code null})
   * @return model path
   */
  public ModelPath concat(ModelPathSpec modelPathSpec) {
    if (modelPathSpec == null) return this;
    return concat(modelPathSpec.value());
  }

  /**
   * Gets the sequence of model types in this path as a list.
   * @return list of model types
   */
  public List<Class<?>> asList() {
    return Collections.unmodifiableList(path);
  }

  /**
   * Gets the sequence of model types in this path as an array
   * @return array of model types
   */
  public Class<?>[] asArray() {
    return path.toArray(new Class<?>[path.size()]);
  }

  /**
   * Gets the match type of the path element at the given index.
   * @param index index of the subject path element
   * @return match type
   */
  public MatchType matchTypeAt(int index) {
    final Class<?> type = path.get(index);
    if (type.equals(AnyModel.class)) {
      return MatchType.ANY;
    }
    else if (type.equals(AnyModelSequence.class)) {
      return MatchType.ANY_SEQUENCE;
    }
    return MatchType.EXACT;
  }

  /**
   * Gets the length of this path.
   * @return path length
   */
  public int length() {
    return path.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (path.size() == 1) {
      return typeToString(path.get(0));
    }

    final StringBuilder sb = new StringBuilder();
    int i = 0;
    sb.append("[");
    for (Class<?> modelClass : path) {
      sb.append(typeToString(modelClass));
      if (++i < path.size()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  private String typeToString(Class<?> type) {
    if (type.equals(AnyModel.class)) {
      return "?";
    }
    else if (type.equals(AnyModelSequence.class)) {
      return "*";
    }
    else {
      return type.getSimpleName();
    }
  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof ModelPath)) return false;
    return Objects.equals(this.path, ((ModelPath) obj).path);
  }

}
