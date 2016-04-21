/*
 * File created on Mar 17, 2016
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
 * Polymorphic type discrimination API
 * <p>
 * When an object or array-of-objects node has a polymorphic type, it is
 * generally necessary to include an attribute in the view identifying the
 * specific subtype that is represented in the node.
 * The {@link org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy}
 * interface specified in this package is implemented to provide the functions
 * necessary to transform a {@link java.lang.Class} object into a type
 * discriminator value and vice-versa.
 * <p>
 * Included in the package is a useful implementation based on the common
 * convention of using the simple (unqualified) class name as a type
 * discriminator.
 */
package org.soulwing.prospecto.api.discriminator;