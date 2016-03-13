/*
 * File created on Mar 13, 2016
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
/**
 * Model type to value type conversion
 * <p>
 * View nodes of value type have a limited range of supported data types
 * (String, Number, Boolean, Enum). This package provides an API for converting
 * between model value types and view value types, and provides some useful
 * implementations for JDK value types (e.g. {@link java.util.Date}). You can
 * write your own {@link org.soulwing.prospecto.api.converter.ValueTypeConverter}
 * implementations to support your model's value types.
 * <p>
 * Converter instances are registered with a
 * {@link org.soulwing.prospecto.api.ViewContext} by putting them to the list
 * returned by {@link org.soulwing.prospecto.api.ViewContext#getValueTypeConverters()}.
 * The converters in the list are consulted in order; the first converter to
 * claim support for a given type will be used for <em>all</em> model instances
 * of that type.
 */
package org.soulwing.prospecto.api.converter;