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

import java.util.ArrayList;
import java.util.List;

/**
 * A type-generalized pattern matcher supporting "glob" expressions.
 * <p>
 * A glob pattern is a sequence of values of type {@code T}.  Among these
 * values, two values are distinguished:
 * <ul>
 * <li> a symbol that is interpreted in a given pattern to mean
 *      <em>match any</em> single value in the input
 * <li> a symbol that is interpreted in a given pattern to mean
 *      <em>match any sequence</em> of input (including an empty sequence)
 * </ul>
 * <p>
 * An input sequence of values of type {@code T} is matched against the pattern
 * by interpreting each value of the input and the pattern in order, applying
 * either <em>equals</em>, <em>match-any</em>, or <em>match-any-sequence</em>
 * according to the pattern specification.
 * <p>
 * When the base type is {@link Character} and the <em>match any</em> symbol is
 * "?" and the <em>match any sequence</em> is "*", the {@link GlobMatcher} takes
 * on the semantics of ordinary filename globbing for string (character array)
 * inputs.
 *
 * @author Carl Harris
 */
public class GlobMatcher<T> {

  private final T anyInputToken;
  private final T anyInputSequenceToken;
  private final T[] pattern;

  /**
   * Constructs a new instance.
   * @param anyInputToken an instance of the {@code T} that will be
   *    interpreted as the symbol used to designate <em>match-any</em>
   * @param anyInputSequenceToken an instance of the {@code T} that will be
   *    interpreted as the symbol used to designate <em>match-any-sequence</em>
   * @param pattern a sequence of values of {@code T} that specify the pattern
   *    to match
   */
  private GlobMatcher(T anyInputToken, T anyInputSequenceToken, T[] pattern) {
    this.anyInputToken = anyInputToken;
    this.anyInputSequenceToken = anyInputSequenceToken;
    this.pattern = pattern;
  }

  public static <T> GlobMatcher<T> with(T anyInputToken, T anyInputSequenceToken,
      T[] pattern) {
    return new GlobMatcher<>(anyInputToken, anyInputSequenceToken, pattern);
  }

  /**
   * Tests the given input as a match for this pattern.
   * @param input the input to test
   * @return {@code true} if {@code input} matches this pattern
   */
  public boolean matches(T[] input) {
    return newMatcher().matches(input);
  }

  /**
   * Tests the given input as a match for this pattern.
   * @param input the input to test
   * @return {@code true} if {@code input} matches this pattern
   */
  @SuppressWarnings("unchecked")
  public boolean matches(List<T> input) {
    return newMatcher().matches((T[]) (input.toArray()));
  }

  private InnerMatcher<T> newMatcher() {
    return new InnerMatcher<>(anyInputToken, anyInputSequenceToken, pattern);
  }

  /**
   * A pattern matching finite state machine.
   * @param <T> base type
   */
  interface Matcher<T> {

    /**
     * Moves the matcher to the next input token and corresponding matcher
     * state.
     */
    void next();

    /**
     * Skips to the next input token, without changing the matcher state.
     */
    void skip();

    /**
     * Terminates the pattern match, with the specified result.
     * @param matches the result of the pattern match
     */
    void terminate(boolean matches);

    /**
     * Tests the given input for a match using the pattern represented by this
     * matcher.
     * @param input the input to test
     * @return {@code true} if {@code input} matches the pattern
     */
    boolean matches(T[] input);

  }

  /**
   * A state for a pattern.
   * <p>
   * Each token in pattern gets an associated state, and these states are
   * considered in sequence as an input is matched to the pattern.
   * @param <T> base type for the matcher
   */
  interface MatcherState<T> {

    /**
     * Attempts to match the given token from the input.
     * @param token input token
     * @param matcher the associated matcher
     */
    void matchToken(T token, Matcher<T> matcher);

  }

  /**
   * A {@link MatcherState} with <em>equals</em> semantics
   * @param <T> the base type
   */
  static class EqualsState<T> implements MatcherState<T> {

    private final T expectedToken;

    public EqualsState(T expectedToken) {
      this.expectedToken = expectedToken;
    }

    @Override
    public void matchToken(T token, Matcher<T> matcher) {
      if (token.equals(expectedToken)) {
        matcher.next();
      }
      else {
        matcher.terminate(false);
      }
    }
  }

  /**
   * A {@link MatcherState} with <em>match-any</em> semantics.
   * @param <T> the base type
   */
  static class MatchAnyState<T> implements MatcherState<T> {

    @Override
    public void matchToken(T token, Matcher<T> matcher) {
      matcher.next();
    }

  }

  /**
   * A {@link MatcherState} with <em>match-any-sequence</em> semantics.
   * @param <T> the base type
   */
  static class MatchAnySequenceState<T> implements MatcherState<T> {

    private final T nextExpectedToken;

    public MatchAnySequenceState(T nextExpectedToken) {
      this.nextExpectedToken = nextExpectedToken;
    }

    @Override
    public void matchToken(T token, Matcher<T> matcher) {
      if (nextExpectedToken.equals(token)) {
        matcher.next();
      }
      else {
        matcher.skip();
      }
    }

  }

  /**
   * A {@link MatcherState} for the special case of <em>match-any-sequence</em>
   * semantics at the end of the pattern.
   */
  static class MatchAnyTrailingSequence<T> implements MatcherState<T> {

    @Override
    public void matchToken(T token, Matcher<T> matcher) {
      matcher.terminate(true);
    }

  }

  /**
   * A simple finite state machine implementation of {@link Matcher}.
   */
  static class InnerMatcher<T> implements Matcher<T> {

    private final List<MatcherState<T>> states = new ArrayList<>();

    private T[] input;
    private int stateIndex;
    private int inputIndex;
    private boolean done;

    /**
     * Constructs a new instance.
     * @param anyInputToken an instance of the {@code T} that will be
     *    interpreted as the symbol used to designate <em>match-any</em>
     * @param anyInputSequenceToken an instance of the {@code T} that will be
     *    interpreted as the symbol used to designate <em>match-any-sequence</em>
     * @param pattern a sequence of values of {@code T} that specify the pattern
     *    to match
     */
    InnerMatcher(T anyInputToken, T anyInputSequenceToken, T[] pattern) {
      int i = 0;
      // parse the pattern into states
      while (i < pattern.length) {
        T token = pattern[i++];
        if (token.equals(anyInputToken)) {
          states.add(new MatchAnyState<T>());
        }
        else if (token.equals(anyInputSequenceToken)) {
          // flatten adjacent match-any-sequence symbols into a single state
          while (token.equals(anyInputSequenceToken) && i < pattern.length) {
            token = pattern[i++];
          }
          states.add(!token.equals(anyInputSequenceToken) ?
              new MatchAnySequenceState<T>(token)
                  : new MatchAnyTrailingSequence<T>());
        }
        else {
          states.add(new EqualsState<T>(token));
        }
      }
    }

    @Override
    public void next() {
      skip();
      stateIndex++;
      if (stateIndex == states.size()) {
        done = true;
      }
    }

    @Override
    public void skip() {
      inputIndex++;
      if (inputIndex == input.length) {
        done = true;
      }
    }

    @Override
    public void terminate(boolean matches) {
      done = true;
      if (matches) {
        inputIndex = input.length;
        stateIndex = states.size();
      }
    }

    @Override
    public boolean matches(T[] input) {
      this.input = input;
      stateIndex = 0;
      inputIndex = 0;
      done = inputIndex == input.length || stateIndex == states.size();
      while (!done) {
        states.get(stateIndex).matchToken(input[inputIndex], this);
      }
      if (inputIndex == input.length && stateIndex < states.size()
          && states.get(stateIndex) instanceof MatchAnyTrailingSequence) {
        stateIndex++;
      }
      return inputIndex == input.length && stateIndex == states.size();
    }

  }

}
