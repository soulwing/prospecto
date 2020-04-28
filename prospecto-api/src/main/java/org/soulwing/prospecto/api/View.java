/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.api;

import java.util.Map;

/**
 * A view is a hierarchical organization of selected information from a model.
 * <p>
 * When a view is evaluated in a given context, it produces a stream of events
 * that can be used to produce a textual representation suitable for conveyance
 * to a client.
 * <p>
 * A view is generated from a {@link ViewTemplate}. An implementation of a view
 * need not be thread safe; a view must not be accessed concurrently by multiple
 * threads.
 *
 * @author Carl Harris
 */
public interface View extends Iterable<View.Event> {

  /**
   * An event for a view.
   */
  interface Event {

    /**
     * Event type
     * <p>
     * These roughly correspond to the structure of an JSON object
     * representation. However, the same event types could be used to
     * produce XML, YAML, etc.
     * <p>
     * The {@link #META} type is used to allow dynamically generated metadata
     * values to be produced in a view, but such events should be treated in
     * a manner that is essentially similar to a {@link #VALUE} event.
     */
    enum Type {
      VALUE, META, DISCRIMINATOR,
      BEGIN_OBJECT, END_OBJECT,
      BEGIN_ARRAY, END_ARRAY;

      /**
       * Gets the complement of this type.
       * @return complementary type
       */
      public Type complement() {
        switch (this) {
          case BEGIN_OBJECT:
            return END_OBJECT;
          case END_OBJECT:
            return BEGIN_OBJECT;
          case BEGIN_ARRAY:
            return END_ARRAY;
          case END_ARRAY:
            return BEGIN_ARRAY;
          default:
            return this;
        }
      }

      /**
       * Tests whether this is the begin event of a complementary pair.
       * @return {@code true} if begin event
       */
      public boolean isBegin() {
        return this == BEGIN_OBJECT || this == BEGIN_ARRAY;
      }

      /**
       * Tests whether this is the end event of a complementary pair.
       * @return {@code true} if end event
       */
      public boolean isEnd() {
        return this == END_OBJECT || this == END_ARRAY;
      }

    }

    /**
     * Gets the event type.
     * @return event type
     */
    Type getType();

    /**
     * Gets the name of the view node associated with this event
     * @return name or {@code null} if the view node that corresponds to this
     *   event has no name
     */
    String getName();

    /**
     * Gets the namespace for {@link #getName() name}.
     * @return name or {@code null} if the namespace for {@link #getName() name}
     *   should be inherited from a prior event or defaulted
     */
    String getNamespace();

    /**
     * Gets the value associated with this event.
     * <p>
     * Only events of type {@link Type#VALUE} and {@link Type#META} have a
     * value, and the value is of a simple type (e.g. String, Number, Date,
     * Boolean, etc). The data type of a an event of type {@link Type#META} is
     * always String.
     *
     * @return value or {@code null} if this event has no associated value
     */
    Object getValue();

  }

  /**
   * An envelope for a view.
   * <p>
   * The envelope allows the attachment of arbitrary name-value
   * pairs with a view. This can be used to associate various metadata with
   * a view. For example, a view that contains a "page" of a large ordered
   * collection of objects could include properties that describe the offset
   * into the collection and the total number of objects available.
   * <p>
   * The way in which envelope properties are incorporated in a textual
   * representation of the view is dependent on the representation format. In
   * JSON, the view could be <em>enveloped</em> such that the envelope
   * properties and the view itself are attributes of an outer wrapper object.
   * In XML, the envelope properties could be attributes of the root element.
   */
  interface Envelope extends Iterable<Map.Entry<String, Object>> {

    /**
     * Puts a property onto the envelope.
     * <p>
     * The given value replaces any existing value associated with {@code name}.
     * @param name name of the property
     * @param value value to associate with {@code name}
     * @return this envelope
     */
    Envelope putProperty(String name, Object value);

    /**
     * Encapsulates the view associated with this envelope in another view.
     * <p>
     * Envelope properties will be reflected as top-level properties of the
     * returned view.
     *
     * @param name name for the view node that will contain the subtree
     *    represented by the view associated with this envelope in the
     *    resulting view
     * @return resulting view
     */
    View seal(String name);

    /**
     * Encapsulates the view associated with this envelope in another view.
     * <p>
     * Envelope properties will be reflected as top-level properties of the
     * returned view.
     *
     * @param name name for the view node that will contain the subtree
     *    represented by the view associated with this envelope in the
     *    resulting view
     * @param namespace namespace for {@code name} or {@code null} to
     *    specify no namespace
     * @return resulting view
     */
    View seal(String name, String namespace);

  }

  /**
   * Gets the envelope for this view.
   * <p>
   * Deprecated: Use {@link #envelope()} instead.
   *
   * @return envelope (never {@code null})
   */
  @Deprecated
  Envelope getEnvelope();

  /**
   * Gets the envelope for this view.
   * @return envelope (never {@code null})
   */
  Envelope envelope();

}
