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
 * Association manager API
 * <p>
 * An association manager is responsible for managing to-one and to-many
 * associations between model types. Default implementations of this API use
 * JavaBeans accessors and the methods of the JDK Collections API to manage
 * associations.
 * <p>
 * If your domain model exposes is own API for managing associations between
 * its model types, you can implement an association manager and register it
 * with a {@link org.soulwing.prospecto.api.ViewContext} when applying views
 * to update your model instances.
 */
package org.soulwing.prospecto.api.association;