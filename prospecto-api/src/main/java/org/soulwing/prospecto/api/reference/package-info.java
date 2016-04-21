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
 * Reference resolver API
 * <p>
 * A view template can incorporate reference nodes for many-to associations
 * between model types. During view application, the model object(s) to
 * associate with corresponding model object are resolved by locating the
 * first {@link org.soulwing.prospecto.api.reference.ReferenceResolver}
 * registered in the view context that claims to support the model type of
 * the reference.
 * <p>
 * Reference resolvers are registered by obtaining the collection of
 * resolvers from the view context using
 * {@link org.soulwing.prospecto.api.ViewContext#getListeners()} and then
 * appending or prepending a resolver as appropriate to the needs of the
 * application.
 */
package org.soulwing.prospecto.api.reference;