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
 * Scope API
 * <p>
 * Scopes are used in throughout Prospecto where a unordered collection of
 * objects, retrievable either by type or by name is required. A view node is
 * a scope, that holds various objects needed when evaluating the node during
 * a view processing cycle. When listeners are invoked during view processing,
 * a stack of scopes is utilized to allow listeners to place objects into a
 * scope that will be visible to other listeners.
 * <p>
 * The view context allows the application to define global scopes as needed.
 */
package org.soulwing.prospecto.api.scope;