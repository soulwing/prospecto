/*
 * File created on Nov 8, 2016
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
 * Object factory API
 * <p>
 * When a view template is used to apply a view to update a model,
 * model object(s) must be instantiated. This API allows factory strategies
 * to be registered by obtaining the collection of factory strategies from
 * the view context using
 * {@link org.soulwing.prospecto.api.ViewContext#getObjectFactories()} and then
 * appending or prepending factory strategies as appropriate to the needs of the
 * application.
 * <p>
 * The registered factories are used by association managers and view
 * applicators whenever a model type must be instantiated to support view
 * application.
 */
package org.soulwing.prospecto.api.factory;