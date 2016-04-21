/*
 * File created on Apr 21, 2016
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
 * Service provider interface
 * <p>
 * This package defines the interfaces implemented by Prospecto service provider.
 * The JDK {@link java.util.ServiceLoader} mechanism is used by Prospecto to
 * locate various runtime components. An implementation of the any of the
 * interfaces in this package should declare its availability using the service
 * registration mechanism defined for the service loader.
 */
package org.soulwing.prospecto.spi;