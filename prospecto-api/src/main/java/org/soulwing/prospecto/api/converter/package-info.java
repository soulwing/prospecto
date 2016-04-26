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
 * Model type to view type conversion API
 * <p>
 * View nodes of value type have a limited range of supported data types;
 * strings, numbers, booleans. Prospecto provides support for automatic
 * coercion of most value types to and from a view representation.
 * See {@link org.soulwing.prospecto.api.converter.Coerce} for more information.
 * <p>
 * For those situations in which a value type cannot be automatically coerced,
 * this package provides an API for converting between value types and an
 * appropriate view representation. The package provides implementations for
 * some JDK value types (e.g. {@link java.util.Date}) as well. You can write
 * your own {@link org.soulwing.prospecto.api.converter.ValueTypeConverter}
 * implementations to support your model's value types.
 * <p>
 * A converter instance can be assigned directly to a value node using the
 * various overloads of the template builder's
 * {@link org.soulwing.prospecto.api.ViewTemplateBuilder#converter(org.soulwing.prospecto.api.converter.ValueTypeConverter) converter} method.
 * Converter instances can also be registered with a view context, to handle
 * all instances of a given value type wherever it appears in any view processed
 * with that context. Add your converters to the list returned by
 * {@link org.soulwing.prospecto.api.ViewContext#getValueTypeConverters()}.
 * The converters in the list are consulted in order; the first converter to
 * claim support for a given value type will be used for <em>all</em> instances
 * of that type.
 */
package org.soulwing.prospecto.api.converter;