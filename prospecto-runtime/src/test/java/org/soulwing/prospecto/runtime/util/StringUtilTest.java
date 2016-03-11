/*
 * File created on Mar 10, 2016
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link StringUtil}.
 *
 * @author Carl Harris
 */
public class StringUtilTest {

  @Test
  public void testEmptyIterable() throws Exception {
    assertThat(StringUtil.join(Collections.<String>emptyList(), '/'),
        is(equalTo("")));
  }

  @Test
  public void testSingletonIterable() throws Exception {
    assertThat(StringUtil.join(Collections.singleton("foo"), '/'),
        is(equalTo("foo")));
  }

  @Test
  public void testSingletonIterableContainsNull() throws Exception {
    assertThat(StringUtil.join(Collections.singleton((String) null), '/'),
        is(equalTo("")));
  }


  @Test
  public void testTwoElementIterable() throws Exception {
    final List<String> elements = new ArrayList<>();
    elements.add("one");
    elements.add("two");
    assertThat(StringUtil.join(elements, '/'), is(equalTo("one/two")));
  }

  @Test
  public void testTwoElementIterableContainsNull() throws Exception {
    final List<String> elements = new ArrayList<>();
    elements.add(null);
    elements.add("two");
    assertThat(StringUtil.join(elements, '/'), is(equalTo("/two")));
  }

}
