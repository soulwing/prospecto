/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.url.runtime.path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.soulwing.prospecto.url.runtime.path.ModelPath;

/**
 * Unit tests for {@link ModelPath}.
 *
 * @author Carl Harris
 */
public class ModelPathTest {

  @Test
  public void testCreate() throws Exception {
    assertThat(ModelPath.with(Object.class).asList(),
      contains((Class) Object.class));
    assertThat(ModelPath.with(
        Integer.class, Long.class).asList(),
        contains((Class) Integer.class, Long.class));
  }

  @Test
  public void testConcat() throws Exception {
    assertThat(ModelPath.with(Integer.class)
            .concat(Long.class).asList(),
        contains((Class) Integer.class, Long.class));
  }

  @Test
  public void testToString() throws Exception {
    assertThat(ModelPath.with(Object.class).toString(),
      is(equalTo(Object.class.getSimpleName())));
    assertThat(ModelPath.with(
        Integer.class, Long.class).toString(),
        is(equalTo("[" + Integer.class.getSimpleName() + ", "
            + Long.class.getSimpleName() + "]")));
  }

}
