/*
 * File created on Apr 1, 2016
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
package org.soulwing.prospecto.runtime.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleLinkedList}.
 * s
 * @author Carl Harris
 */
public class SimpleLinkedListTest {

  private final SimpleLinkedList<Object> list = new SimpleLinkedList<>();

  @Test
  public void testAppendPrependAndRemove() throws Exception {
    final Object element0 = new Object();
    final Object element1 = new Object();
    final Object element2 = new Object();

    list.append(element1);
    list.prepend(element0);
    list.append(element2);
    assertThat(list.toList(), contains(element0, element1, element2));

    assertThat(list.remove(element2), is(true));
    assertThat(list.toList(), contains(element0, element1));
  }

}
