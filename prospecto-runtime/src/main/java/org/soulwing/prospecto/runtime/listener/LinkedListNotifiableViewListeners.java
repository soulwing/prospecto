/*
 * File created on Mar 23, 2016
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
package org.soulwing.prospecto.runtime.listener;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.listener.ViewListener;
import org.soulwing.prospecto.api.listener.ViewNodeAcceptor;
import org.soulwing.prospecto.api.listener.ViewNodeEntityListener;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodeListener;
import org.soulwing.prospecto.api.listener.ViewNodePropertyAcceptor;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyInterceptor;
import org.soulwing.prospecto.api.listener.ViewNodePropertyListener;

/**
 * A {@link NotifiableViewListeners} implementation backed by a
 * {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListNotifiableViewListeners
    implements NotifiableViewListeners {

  private final List<ViewListener> listeners = new LinkedList<>();

  @Override
  public boolean shouldVisitNode(ViewNodeEvent event) {
    boolean visit = true;
    for (final ViewListener listener : listeners) {
      if (ViewNodeAcceptor.class
          .isAssignableFrom(listener.getClass())) {
        visit = ((ViewNodeAcceptor) listener).shouldVisitNode(event);
        if (!visit) break;
      }
    }
    return visit;
  }

  @Override
  public void nodeVisited(ViewNodeEvent event) {
    for (final ViewListener listener : listeners) {
      if (ViewNodeListener.class
          .isAssignableFrom(listener.getClass())) {
        ((ViewNodeListener) listener).nodeVisited(event);
      }
    }
  }

  @Override
  public boolean shouldVisitProperty(ViewNodePropertyEvent event) {
    boolean visit = true;
    for (final ViewListener listener : listeners) {
      if (ViewNodePropertyAcceptor.class
          .isAssignableFrom(listener.getClass())) {
        visit = ((ViewNodePropertyAcceptor) listener).shouldVisitProperty(event);
        if (!visit) break;
      }
    }
    return visit;
  }

  @Override
  public Object didExtractValue(ViewNodePropertyEvent event) {
    Object value = event.getValue();
    for (final ViewListener listener : listeners) {
      if (ViewNodePropertyInterceptor.class
          .isAssignableFrom(listener.getClass())) {
        value = ((ViewNodePropertyInterceptor) listener).didExtractValue(
            new ViewNodePropertyEvent(event, value));
      }
    }
    return value;
  }

  @Override
  public Object willInjectValue(ViewNodePropertyEvent event) {
    Object value = event.getValue();
    for (final ViewListener listener : listeners) {
      if (ViewNodePropertyInterceptor.class
          .isAssignableFrom(listener.getClass())) {
        value = ((ViewNodePropertyInterceptor) listener).willInjectValue(
            new ViewNodePropertyEvent(event, value));
      }
    }
    return value;
  }

  @Override
  public void propertyVisited(ViewNodePropertyEvent event) {
    for (final ViewListener listener : listeners) {
      if (ViewNodePropertyListener.class
          .isAssignableFrom(listener.getClass())) {
        ((ViewNodePropertyListener) listener).propertyVisited(event);
      }
    }
  }

  @Override
  public void entityCreated(ViewNodePropertyEvent event) {
    for (final ViewListener listener : listeners) {
      if (ViewNodeEntityListener.class.isAssignableFrom(listener.getClass())) {
        ((ViewNodeEntityListener) listener).entityCreated(event);
      }
    }
  }

  @Override
  public void entityDiscarded(ViewNodePropertyEvent event) {
    for (final ViewListener listener : listeners) {
      if (ViewNodeEntityListener.class.isAssignableFrom(listener.getClass())) {
        ((ViewNodeEntityListener) listener).entityDiscarded(event);
      }
    }
  }

  @Override
  public void append(ViewListener listener) {
    listeners.add(listener);
  }

  @Override
  public void prepend(ViewListener listener) {
    listeners.add(0, listener);
  }

  @Override
  public boolean remove(ViewListener listener) {
    return listeners.remove(listener);
  }

  @Override
  public List<ViewListener> toList() {
    return listeners;
  }

}
