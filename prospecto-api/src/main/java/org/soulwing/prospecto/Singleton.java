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
package org.soulwing.prospecto;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A factory that produces a singleton instance of a given type.
 *
 * @author Carl Harris
 */
abstract class Singleton<T> {

  private final Lock lock = new ReentrantLock();

  private T instance;

  public T getInstance() {
    if (instance == null) {
      lock.lock();
      try {
        if (instance == null) {
          instance = newInstance();
        }
      }
      finally {
        lock.unlock();
      }
    }
    return instance;
  }

  protected abstract T newInstance();

}
