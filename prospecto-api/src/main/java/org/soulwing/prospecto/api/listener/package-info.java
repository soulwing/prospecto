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
 * View node listener API
 *
 * When generating a view or applying a view to update a model, several types
 * of listeners can be use to allow application code to observe and/or
 * manipulate the process. All listeners implement the
 * {@link org.soulwing.prospecto.api.listener.ViewListener} marker interface
 * and are registered with the context by retrieving the view context's
 * listener collection (using
 * {@link org.soulwing.prospecto.api.ViewContext#getListeners()}) and appending
 * or prepending listener instances as appropriate to the needs of the
 * application.
 * <p>
 * See the various listener subtypes in the package for a description of
 * when and under what conditions a listener of that type is invoked during the
 * view processing lifecycle.
 */
package org.soulwing.prospecto.api.listener;