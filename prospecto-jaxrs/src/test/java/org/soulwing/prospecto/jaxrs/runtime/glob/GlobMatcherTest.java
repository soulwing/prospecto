/*
 * File created on Mar 11, 2016
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
package org.soulwing.prospecto.jaxrs.runtime.glob;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link GlobMatcher}.
 *
 * @author Carl Harris
 */
public class GlobMatcherTest {

  @Test
  public void testExactMatch() throws Exception {
    GlobMatcher<Character> matcher = matcher("abc");

    assertThat(matcher.matches(new Character[0]), is(false));
    assertThat(matcher.matches(stringToArray("a")), is(false));
    assertThat(matcher.matches(stringToArray("ab")), is(false));
    assertThat(matcher.matches(stringToArray("abc")), is(true));
    assertThat(matcher.matches(stringToArray("abcd")), is(false));
  }

  @Test
  public void testAnyInputMatch() throws Exception {
    assertThat(matcher("?b").matches(stringToArray("ab")), is(true));
    assertThat(matcher("a?").matches(stringToArray("ab")), is(true));
    assertThat(matcher("?b").matches(stringToArray("ab")), is(true));
    assertThat(matcher("??").matches(stringToArray("ab")), is(true));
    assertThat(matcher("??").matches(stringToArray("a")), is(false));
    assertThat(matcher("??").matches(stringToArray("abc")), is(false));
    assertThat(matcher("?").matches(new Character[0]), is(false));
  }

  @Test
  public void testAnyInputSequenceMatch() throws Exception {
    assertThat(matcher("*").matches(new Character[0]), is(true));
    assertThat(matcher("*").matches(stringToArray("a")), is(true));
    assertThat(matcher("*").matches(stringToArray("ab")), is(true));
    assertThat(matcher("a*").matches(new Character[0]), is(false));
    assertThat(matcher("a*").matches(stringToArray("a")), is(true));
    assertThat(matcher("a*").matches(stringToArray("ab")), is(true));
    assertThat(matcher("a*").matches(stringToArray("abc")), is(true));
    assertThat(matcher("a*").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("*d").matches(new Character[0]), is(false));
    assertThat(matcher("*d").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("*d").matches(stringToArray("bcd")), is(true));
    assertThat(matcher("*d").matches(stringToArray("cd")), is(true));
    assertThat(matcher("*d").matches(stringToArray("d")), is(true));
    assertThat(matcher("a*d").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("a*d").matches(stringToArray("abd")), is(true));
    assertThat(matcher("a*d").matches(stringToArray("ad")), is(true));
    assertThat(matcher("a*d").matches(stringToArray("ab")), is(false));
    assertThat(matcher("a*d").matches(stringToArray("cd")), is(false));
    assertThat(matcher("a**").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("**d").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("a**d").matches(stringToArray("abcd")), is(true));
    assertThat(matcher("a**d?").matches(stringToArray("abcd")), is(false));
    assertThat(matcher("a**d?").matches(stringToArray("abcde")), is(true));
  }

  private GlobMatcher<Character> matcher(String pattern) {
    return GlobMatcher.<Character>with('?', '*', stringToArray(pattern));
  }

  private Character[] stringToArray(String s) {
    Character[] tokens = new Character[s.length()];
    for (int i = 0; i < s.length(); i++) {
      tokens[i] = s.charAt(i);
    }
    return tokens;
  }

}
