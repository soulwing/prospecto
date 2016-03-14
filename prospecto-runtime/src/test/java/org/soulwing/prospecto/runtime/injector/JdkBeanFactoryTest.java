/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.injector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.Test;

/**
 * Tests for {@link JdkBeanFactory}.
 *
 * @author Carl Harris
 */
public class JdkBeanFactoryTest {

  private static final String STRING = "string";
  private static final int INT = -1;
  private static final String STRING_PROPERTY = "stringProperty";
  private static final String INT_PROPERTY = "intProperty";
  private JdkBeanFactory beanFactory = new JdkBeanFactory();

  @Test
  public void testConstructWithArray() throws Exception {
    MockBean bean = beanFactory.construct(MockBean.class,
        STRING_PROPERTY, STRING, INT_PROPERTY, INT);
    assertThat(bean.getStringProperty(), is(equalTo(STRING)));
    assertThat(bean.getIntProperty(), is(equalTo(INT)));
    assertThat(bean.initialized, is(true));
  }

  @Test(expected = RuntimeException.class)
  public void testConstructWithArrayOfUnevenLength() throws Exception {
    MockBean bean = beanFactory.construct(MockBean.class,
        STRING_PROPERTY, STRING, INT_PROPERTY, INT, 0);
  }

  @Test
  public void testConstructWithMap() throws Exception {
    Map<String, Object> properties = new HashMap<>();
    properties.put(STRING_PROPERTY, STRING);
    properties.put(INT_PROPERTY, INT);

    MockBean bean = beanFactory.construct(MockBean.class, properties);
    assertThat(bean.getStringProperty(), is(equalTo(STRING)));
    assertThat(bean.getIntProperty(), is(equalTo(INT)));
    assertThat(bean.initialized, is(true));
  }

  public static class MockBean {

    private String stringProperty;
    private int intProperty;
    private boolean initialized;

    @PostConstruct
    public void init() {
      initialized = true;
    }

    /**
     * Gets the {@code stringProperty} property.
     * @return property value
     */
    public String getStringProperty() {
      return stringProperty;
    }

    /**
     * Sets the {@code stringProperty} property.
     * @param stringProperty the property value to set
     */
    public void setStringProperty(String stringProperty) {
      this.stringProperty = stringProperty;
    }

    /**
     * Gets the {@code intProperty} property.
     * @return property value
     */
    public int getIntProperty() {
      return intProperty;
    }

    /**
     * Sets the {@code intProperty} property.
     * @param intProperty the property value to set
     */
    public void setIntProperty(int intProperty) {
      this.intProperty = intProperty;
    }

  }

}
