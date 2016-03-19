/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.text;

import java.io.OutputStream;
import java.util.Iterator;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.ViewWriter;

/**
 * An object that produces a textual representation of a {@link View} on
 * an {@link OutputStream}.
 * <p>
 * This class is designed to allow a callback-driven interpretation of a
 * view's event stream.
 * <p>
 * A writer is <em>not</em> thread safe.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewWriter implements ViewWriter {

  private final View view;
  private final OutputStream outputStream;

  /**
   * Constructs a new writer
   * @param view the subject view
   *
   */
  protected AbstractViewWriter(View view, OutputStream outputStream) {
    this.view = view;
    this.outputStream = outputStream;
  }

  /**
   * Writes the view to the target output stream.
   * <p>
   * The underlying output stream is <em>not</em> closed by this method.
   * @throws ViewException if an error occurs in writing the view.
   * @throws IllegalStateException if this method has already been invoked on
   *   this writer instance
   */
  @Override public final void writeView() throws ViewException {
    try {
      beforeViewEvents(outputStream);
      final Iterator<View.Event> events = view.iterator();
      while (events.hasNext()) {
        final View.Event event = events.next();
        switch (event.getType()) {
          case BEGIN_OBJECT:
            onBeginObject(event);
            break;
          case END_OBJECT:
            onEndObject(event);
            break;
          case BEGIN_ARRAY:
            onBeginArray(event);
            break;
          case END_ARRAY:
            onEndArray(event);
            break;
          case VALUE:
            onValue(event);
            break;
          case DISCRIMINATOR:
            onDiscriminator(event);
            break;
          case URL:
            onUrl(event);
            break;
          default:
            throw new IllegalStateException("unrecognized event type: "
                + event.getType().name());
        }
      }
      afterViewEvents();
    }
    catch (ViewException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ViewException(ex);
    }
  }

  /**
   * Notifies the recipient that the view's event stream will start.
   * @param outputStream target output stream for the view's textual
   *    representation
   */
  protected void beforeViewEvents(OutputStream outputStream) throws Exception {
  }

  /**
   * Notifies the recipient that the view's event stream has ended.
   * @throws Exception
   */
  protected void afterViewEvents() throws Exception {
  }

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#BEGIN_OBJECT}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onBeginObject(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#END_OBJECT}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onEndObject(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#BEGIN_ARRAY}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onBeginArray(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#END_ARRAY}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onEndArray(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#VALUE}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onValue(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#URL}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onUrl(View.Event event) throws Exception;

  /**
   * Notifies the recipient that the next {@code event} is of type
   * {@link View.Event.Type#DISCRIMINATOR}.
   * @param event the subject event
   * @throws Exception
   */
  protected abstract void onDiscriminator(View.Event event) throws Exception;

  /**
   * Gets the source view for this writer.
   * @return source view
   */
  protected final View getView() {
    return view;
  }

}
