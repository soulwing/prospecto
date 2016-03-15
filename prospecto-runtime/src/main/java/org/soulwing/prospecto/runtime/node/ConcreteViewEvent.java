/*
 * File created on Mar 15, 2016
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
package org.soulwing.prospecto.runtime.node;

import org.soulwing.prospecto.api.View;

/**
 * A {@link View.Event} implementation.
 *
 * @author Carl Harris
 */
class ConcreteViewEvent implements View.Event {

  private final Type type;
  private final String name;
  private final String namespace;
  private final Object value;

  public ConcreteViewEvent(Type type, String name, String namespace) {
    this(type, name, namespace, null);
  }

  public ConcreteViewEvent(Type type, String name, String namespace,
      Object value) {
    this.type = type;
    this.name = name;
    this.namespace = namespace;
    this.value = value;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "ConcreteViewEvent{" +
        "type=" + type +
        ", name=" + name +
        ", namespace=" + namespace +
        ", value=" + value +
        '}';
  }

}
